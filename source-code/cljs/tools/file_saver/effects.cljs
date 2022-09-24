
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.file-saver.effects
    (:require [tools.file-saver.prototypes :as prototypes]
              [tools.file-saver.views      :as views]
              [x.app-core.api              :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-saver/save-file!
  ; @param (keyword)(opt) saver-id
  ; @param (map) saver-props
  ;  {:data-url (string)(opt)
  ;    Only w/o {:uri ...}
  ;   :filename (string)(opt)
  ;    Default: DEFAULT-FILENAME
  ;   :uri (string)(opt)
  ;    Only w/o {:data-url ...}}
  ;
  ; @usage
  ;  [:file-saver/save-file! {...}]
  ;
  ; @usage
  ;  [:file-saver/save-file! :my-file-saver {...}]
  ;
  ; @usage
  ;  [:file-saver/save-file! {:data-url "data:text/plain;charset=utf-8,..."}
  ;                           :filename "my-file.edn"}]
  ;
  ; @usage
  ;  [:file-saver/save-file! {:uri      "/images/my-image.jpg"}
  ;                           :filename "my-image.jpg"}]
  [a/event-vector<-id]
  (fn [_ [_ saver-id saver-props]]
      (let [saver-props (prototypes/saver-props-prototype saver-props)]
           [:file-saver/render-dialog! saver-id saver-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-saver/render-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Párbeszédablakot nyit meg a fájl mentésével kapcsolatban.
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  (fn [_ [_ saver-id saver-props]]
      [:ui/render-popup! :file-saver/view
                         {:content [views/view saver-id saver-props]}]))
