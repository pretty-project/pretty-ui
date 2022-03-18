
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.css-handler.side-effects
    (:require [dom.api                               :as dom]
              [mid-fruits.string                     :as string]
              [x.app-core.api                        :as a :refer [cache-control-uri]]
              [x.app-environment.css-handler.helpers :as css-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-external-css!
  ; @param (string) filepath
  ; @param (map)(opt) context-props
  ;  {:as-first? (boolean)}
  ;
  ; @usage
  ;  (environment/add-external-css! "/css/filename.css")
  [filepath context-props]
  (let [head-element (dom/get-head-element)
        app-build    (a/app-build)
        filepath     (cache-control-uri (string/starts-with! filepath "/") app-build)
        link-element (css-handler.helpers/create-link-element! filepath)]
       (if-not (css-handler.helpers/source-exists?       head-element filepath)
               (css-handler.helpers/insert-link-element! head-element link-element context-props))))

(defn add-css!
  ; @param (string) filename
  ; @param (map)(opt) context-props
  ;  {:as-first? (boolean)}
  ;
  ; @usage
  ;  (environment/add-css! "/filename.css")
  [filename context-props]
  (let [filepath (css-handler.helpers/filename->external-css-uri filename)]
       (add-external-css! filepath context-props)))

(defn remove-css!
  ; @param (string) filename
  ;
  ; @usage
  ;  (environment/remove-css! "/filename.css")
  [filename])
  ; TODO ...



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/add-external-css! "/css/filename.css"]
(a/reg-fx :environment/add-external-css! add-external-css!)

; @usage
;  [:environment/add-css! "filename.css"]
;
; @usage
;  [:environment/add-css! "filename.css" {...}]
(a/reg-fx :environment/add-css! add-css!)

; @usage
;  [:environment/remove-css! "/filename.css"]
(a/reg-fx :environment/remove-css! remove-css!)
