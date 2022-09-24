
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.file-saver.sample
    (:require [tools.file-saver.api]
              [x.app-core.api :as a]))



;; -- Szöveg mentése fájlként a kliens eszközre -------------------------------
;; ----------------------------------------------------------------------------

(def my-text     (str "My text"))
(def my-data-url (str "data:text/plain;charset=utf-8," text))

(a/reg-event-fx
  :save-my-text!
  [:file-saver/save-file! {:data-url data-url
                           :filename "My file.txt"}])



;; -- Távoli fájl mentése a kliens eszközre -----------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :save-my-file!
  [:file-saver/save-file! {:uri      "/images/my-image.jpg"
                           :filename "my-image.jpg"}])
