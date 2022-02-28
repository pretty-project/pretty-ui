
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.file-saver.effects
    (:require [mid-fruits.candy :refer [param]]
              [x.app-core.api   :as a]
              [x.app-tools.file-saver.engine :as file-saver.engine]
              [x.app-tools.file-saver.views  :as file-saver.views]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- saver-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) saver-props
  ;
  ; @return (map)
  ;  {:filename (string)}
  [saver-props]
  (merge {:filename file-saver.engine/DEFAULT-FILENAME}
         (param saver-props)))

  

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
      (let [saver-props (saver-props-prototype saver-props)]
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
      [:ui/add-popup! saver-id
                      {:body   [file-saver.views/body   saver-id saver-props]
                       :header [file-saver.views/header saver-id saver-props]}]))
