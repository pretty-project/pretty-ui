
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.file-saver.effects
    (:require [x.app-core.api                    :as a]
              [x.app-tools.file-saver.prototypes :as file-saver.prototypes]
              [x.app-tools.file-saver.views      :as file-saver.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :tools/save-file!
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
  ;  [:tools/save-file! {...}]
  ;
  ; @usage
  ;  [:tools/save-file! :my-file-saver {...}]
  ;
  ; @usage
  ;  [:tools/save-file! {:data-url "data:text/plain;charset=utf-8,..."}
  ;                      :filename "my-file.edn"}]
  ;
  ; @usage
  ;  [:tools/save-file! {:uri      "/images/my-image.jpg"}
  ;                      :filename "my-image.jpg"}]
  [a/event-vector<-id]
  (fn [_ [_ saver-id saver-props]]
      (let [saver-props (file-saver.prototypes/saver-props-prototype saver-props)]
           [:tools/render-save-file-dialog! saver-id saver-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :tools/render-save-file-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Párbeszédablakot nyit meg a fájl mentésével kapcsolatban.
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  (fn [_ [_ saver-id saver-props]]
      [:ui/render-popup! :tools.file-saver/view
                         {:content [file-saver.views/view saver-id saver-props]}]))
