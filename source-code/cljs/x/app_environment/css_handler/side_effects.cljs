
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.22
; Description:
; Version: v1.3.8
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.css-handler.side-effects
    (:require [app-fruits.dom    :as dom]
              [mid-fruits.string :as string]
              [x.app-core.api    :as a :refer [cache-control-uri]]
              [x.app-environment.css-handler.engine :as engine]))



;; -- Side-effect events ------------------------------------------------------
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
        link-element (engine/create-link-element! filepath)]
       (if-not (engine/source-exists?       head-element filepath)
               (engine/insert-link-element! head-element link-element context-props))))

(defn add-css!
  ; @param (string) filename
  ; @param (map)(opt) context-props
  ;  {:as-first? (boolean)}
  ;
  ; @usage
  ;  (environment/add-css! "/filename.css")
  [filename context-props]
  (let [filepath (engine/filename->external-css-uri filename)]
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
