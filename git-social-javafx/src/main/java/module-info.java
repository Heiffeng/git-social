module site.achun.git.social {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    requires atlantafx.base;

    requires org.eclipse.jgit;
    requires com.alibaba.fastjson2;


    opens site.achun.git.social to javafx.fxml;
    opens site.achun.git.social.views to javafx.fxml;
    opens site.achun.git.social.data;

    exports site.achun.git.social;
    exports site.achun.git.social.views;
}
