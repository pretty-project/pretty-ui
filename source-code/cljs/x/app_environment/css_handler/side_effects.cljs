

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.css-handler.side-effects
    (:require [dom.api                               :as dom]
              [mid-fruits.string                     :as string]
              [x.app-core.api                        :as a :refer [cache-control-uri]]
              [x.app-environment.css-handler.helpers :as css-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-css!
  ; @param (string) uri
  ; @param (map)(opt) options
  ;  {:as-first? (boolean)}
  ;
  ; @usage
  ;  (environment/add-css! "/css/filename.css")
  [uri options]
  (let [head-element (dom/get-head-element)
        app-build    (a/app-build)
        filepath     (cache-control-uri (string/starts-with! uri "/") app-build)
        link-element (css-handler.helpers/create-link-element! uri)]
       (if-not (css-handler.helpers/source-exists?       head-element uri)
               (css-handler.helpers/insert-link-element! head-element link-element options))))

(defn remove-css!
  ; @param (string) uri
  ;
  ; @usage
  ;  (environment/remove-css! "/css/filename.css")
  [uri])
  ; TODO ...



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/add-css! "/css/filename.css"]
(a/reg-fx :environment/add-css! add-css!)

; @usage
;  [:environment/remove-css! "/filename.css"]
(a/reg-fx :environment/remove-css! remove-css!)
