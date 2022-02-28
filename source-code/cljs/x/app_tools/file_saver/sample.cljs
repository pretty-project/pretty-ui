
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.file-saver.sample
    (:require [x.app-core.api :as a]))



;; -- Szöveg mentése fájlként a kliens eszközre -------------------------------
;; ----------------------------------------------------------------------------

(def my-text     (str "My text"))
(def my-data-url (str "data:text/plain;charset=utf-8," text))

(a/reg-event-fx
  :save-my-text!
  [:tools/save-file! {:data-url data-url
                      :filename "My file.txt"}])



;; -- Távoli fájl mentése a kliens eszközre -----------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :save-my-file!
  [:tools/save-file! {:uri      "/images/my-image.jpg"
                      :filename "my-image.jpg"}])
