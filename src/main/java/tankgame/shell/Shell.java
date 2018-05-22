package tankgame.shell;

import tankgamegui.enums.ShellType;

public class Shell {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ShellType getShellType() {
        return shellType;
    }

    public void setShellType(ShellType shellType) {
        this.shellType = shellType;
    }

    private ShellType shellType;

    public Shell(String message, ShellType shellType) {
        this.message = message;
        this.shellType = shellType;
    }
}