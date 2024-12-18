package frame;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class PlaybackDialog {
    private JDialog dialog;
    private JLabel trackLabel;
    private JProgressBar progressBar;
    private JLabel timeLabel;

    private Timer timer;
    private int elapsedTime; // In seconds
    private final int totalTimeInSeconds; // Total time in seconds
    private boolean isComplete; // Track completion flag

    // Constructor, where totalTime is in seconds
    public PlaybackDialog(String trackName, int totalTimeInSeconds) {
        this.isComplete = false;
        this.totalTimeInSeconds = totalTimeInSeconds;

        dialog = new JDialog();
        dialog.setTitle("Playing: " + trackName);
        dialog.setSize(400, 150);
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);

        // Add components
        trackLabel = new JLabel("Title: " + trackName, SwingConstants.CENTER);
        trackLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dialog.add(trackLabel, BorderLayout.NORTH);

        progressBar = new JProgressBar(0, totalTimeInSeconds);
        progressBar.setStringPainted(true);
        dialog.add(progressBar, BorderLayout.CENTER);

        timeLabel = new JLabel("0:00 -----------||----------- " + formatTime(totalTimeInSeconds), SwingConstants.CENTER);
        dialog.add(timeLabel, BorderLayout.SOUTH);

        dialog.setVisible(true);

        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        elapsedTime = 0;

        // Set the timer to run every second
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (elapsedTime >= totalTimeInSeconds) {
                    timer.cancel();
                    SwingUtilities.invokeLater(() -> {
                        // Mark as complete
                        isComplete = true;
                        dialog.dispose();
                    });
                } else {
                    elapsedTime++; // Increment by 1 second
                    SwingUtilities.invokeLater(() -> updateProgress());
                }
            }
        }, 0, 1000); // Run every second (1000ms)
    }

    private void updateProgress() {
        progressBar.setValue(elapsedTime);
        timeLabel.setText(formatTime(elapsedTime) + " -----------||----------- " + formatTime(totalTimeInSeconds));
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }

    public boolean isComplete() {
        return isComplete;
    }
}
