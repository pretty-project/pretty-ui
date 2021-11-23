
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.31
; Description:
; Version: v0.7.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.temporary-component
    (:require [app-fruits.dom     :as dom]
              [app-fruits.reagent :as reagent]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage W/o callback
;  (ns my-namespace (:require [x.app-tools.api :as tools]))
;
;  (defn my-component [props])
;  (tools/append-temporary-component! [my-component {...}])

; @usage W/ callback
;  (ns my-namespace (:require [x.app-tools.api :as tools]))
;
;  (defn my-button [my-props] [:a {:href "foo/bar"}])
;  (defn click-my-button! [] ...)
;  (tools/append-temporary-component! [my-button {...}] click-my-button!)

; @usage
;  (ns my-namespace (:require [x.app-tools.api :as tools]))
;
;  (defn my-component [props])
;  (tools/append-temporary-component! [my-component {...}])
;  (tools/remove-temporary-component!)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def ENVIRONMENT-ELEMENT-ID "x-app-ui-structure")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-environment-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (DOM-element)
  []
  (dom/get-element-by-id ENVIRONMENT-ELEMENT-ID))

(defn- create-temporary-container!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (DOM-element)
  []
  (let [environment-element (get-environment-element)
        temporary-container (dom/create-element! "DIV")]
       (dom/set-element-id! temporary-container "x-temporary-component")
       (dom/append-element! environment-element temporary-container)))

(defn- remove-temporary-container!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [temporary-container (dom/get-element-by-id "x-temporary-component")]
          (let [environment-element (get-environment-element)]
               (dom/remove-child! environment-element temporary-container))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn append-temporary-component!
  ; @param (component) component
  ; @param (function)(opt) render-callback
  ;
  ; @usage W/o callback
  ;  (defn my-component [props])
  ;  (tools/append-temporary-component! [my-component {...}])
  ;
  ; @usage W/ callback
  ;  (defn my-button [my-props] [:a {:href "foo/bar"}])
  ;  (defn click-my-button! [] ...)
  ;  (tools/append-temporary-component! [my-button {...}] click-my-button!)
  ;
  ; @return (component)
  [component & [render-callback]]
  (remove-temporary-container!)
  (create-temporary-container!)
  (let [temporary-container (dom/get-element-by-id "x-temporary-component")]
       (reagent/render component temporary-container render-callback)))

(defn remove-temporary-component!
  ; @usage
  ;  (tools/remove-temporary-component!)
  []
  (remove-temporary-container!))
