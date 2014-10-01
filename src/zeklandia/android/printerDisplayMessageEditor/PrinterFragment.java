package zeklandia.android.printerDisplayMessageEditor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.MessageFormat;

public class PrinterFragment extends Fragment {

    private static final int printerPort = 9100;
    private final static String formatRDYMSG = "\u001B%-12345X@PJL JOB\n" +
            "@PJL RDYMSG DISPLAY=\"{0}\"\n@PJL EOJ\n\u001B%-12345X\n";
    private final static String formatOPMSG = "\u001B%-12345X@PJL JOB\n" +
            "@PJL OPMSG DISPLAY=\"{0}\"\n@PJL EOJ\n\u001B%-12345X\n";
    private final static String formatERRMSG = "\u001B%-12345X@PJL JOB\n" +
            "@PJL ERRMSG DISPLAY=\"{0}\"\n@PJL EOJ\n\u001B%-12345X\n";
    static EditText editTextIP;
    static EditText editTextMessage;
    CharSequence successMessage = "Message Sent";
    CharSequence errorMessage = "An Error Occurred";

    public static PrinterFragment newInstance(int index) {
        PrinterFragment fragment = new PrinterFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton send = (ImageButton) getView().findViewById(R.id.imageButton1);

        send.setOnClickListener(new OnClickListener() {
            void onClicked(View v) {

            }

            @Override
            public void onClick(View v) {


                EditText ip = (EditText) getActivity().findViewById(R.id.editTextIP);
                String ipString = ((EditText) ip).getText().toString();

                EditText message = (EditText) getActivity().findViewById(R.id.editTextMessage);
                String messageString = ((EditText) message).getText().toString();

                RadioGroup messageType = (RadioGroup) getActivity().findViewById(R.id.radioGroup1);
                RadioButton messageTypeRDY = (RadioButton) getActivity().findViewById(R.id.radioButtonRDY);
                RadioButton messageTypeERR = (RadioButton) getActivity().findViewById(R.id.radioButtonERR);
                RadioButton messageTypeOP = (RadioButton) getActivity().findViewById(R.id.radioButtonOP);
                int radioButtonID = messageType.getCheckedRadioButtonId();
                View radioButton = messageType.findViewById(radioButtonID);

                if (messageType.indexOfChild(radioButton) == 0) {
                    new sendRDYMSGTask().execute(ipString, messageString);
                } else if (messageType.indexOfChild(radioButton) == 1) {
                    new sendERRMSGTask().execute(ipString, messageString);
                } else if (messageType.indexOfChild(radioButton) == 2) {
                    new sendOPMSGTask().execute(ipString, messageString);
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.main, container, false);
    }

    private class sendRDYMSGTask extends AsyncTask<String, Void, Integer> {
        public static final int TOO_LONG_ERROR = 0;
        public static final int CONNECTION_ERROR = 1;
        public static final int SUCESS = 2;

        protected Integer doInBackground(String... params) {
            return sendRDYMSG(params[0], params[1]);
        }

        protected void onPostExecute(Integer result) {
            switch (result) {
                case SUCESS:
                    Toast.makeText(getActivity().getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                    break;
                case CONNECTION_ERROR:
                    Toast.makeText(getActivity().getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        int sendRDYMSG(String ipString, String messageString) {
            try {
                Socket socket = new Socket(ipString, printerPort);
                OutputStream out = socket.getOutputStream();
                out.write(MessageFormat.format(formatRDYMSG, messageString).getBytes());
                out.close();
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return CONNECTION_ERROR;
            } catch (IOException e) {
                e.printStackTrace();
                return CONNECTION_ERROR;
            }
            return SUCESS;
        }
    }

    private class sendERRMSGTask extends AsyncTask<String, Void, Integer> {
        public static final int TOO_LONG_ERROR = 0;
        public static final int CONNECTION_ERROR = 1;
        public static final int SUCESS = 2;

        protected Integer doInBackground(String... params) {
            return sendERRMSG(params[0], params[1]);
        }

        protected void onPostExecute(Integer result) {
            switch (result) {
                case SUCESS:
                    Toast.makeText(getActivity().getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                    break;
                case CONNECTION_ERROR:
                    Toast.makeText(getActivity().getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        int sendERRMSG(String ipString, String messageString) {
            try {
                Socket socket = new Socket(ipString, printerPort);
                OutputStream out = socket.getOutputStream();
                out.write(MessageFormat.format(formatERRMSG, messageString).getBytes());
                out.close();
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return CONNECTION_ERROR;
            } catch (IOException e) {
                e.printStackTrace();
                return CONNECTION_ERROR;
            }
            return SUCESS;
        }
    }

    private class sendOPMSGTask extends AsyncTask<String, Void, Integer> {
        public static final int TOO_LONG_ERROR = 0;
        public static final int CONNECTION_ERROR = 1;
        public static final int SUCESS = 2;

        protected Integer doInBackground(String... params) {
            return sendOPMSG(params[0], params[1]);
        }

        protected void onPostExecute(Integer result) {
            switch (result) {
                case SUCESS:
                    Toast.makeText(getActivity().getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                    break;
                case CONNECTION_ERROR:
                    Toast.makeText(getActivity().getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        int sendOPMSG(String ipString, String messageString) {
            try {
                Socket socket = new Socket(ipString, printerPort);
                OutputStream out = socket.getOutputStream();
                out.write(MessageFormat.format(formatOPMSG, messageString).getBytes());
                out.close();
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return CONNECTION_ERROR;
            } catch (IOException e) {
                e.printStackTrace();
                return CONNECTION_ERROR;
            }
            return SUCESS;
        }
    }
}