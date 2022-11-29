
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.file-saver.sample
    (:require [tools.file-saver.api]
              [re-frame.api :as r]))



;; -- Szöveg mentése fájlként a kliens eszközre -------------------------------
;; ----------------------------------------------------------------------------

(def my-text     (str "My text"))
(def my-data-url (str "data:text/plain;charset=utf-8," text))

(r/reg-event-fx :save-my-text!
  [:file-saver/save-file! {:data-url my-data-url
                           :filename "My file.txt"}])



;; -- Távoli fájl mentése a kliens eszközre -----------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :save-my-file!
  [:file-saver/save-file! {:uri      "/images/my-image.jpg"
                           :filename "my-image.jpg"}])
