package martinezruiz.javier.pmdm003.models;

public class ErrorData {

    public ErrorData(String error) {
        this.error = error;
        this.handled = false;
    }

    String error;
    Boolean handled;

    public boolean isHandled(){
        return handled;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setHandled(Boolean handled) {
        this.handled = handled;
    }
}
