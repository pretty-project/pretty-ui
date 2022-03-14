
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.temporary-component.helpers
    (:require [app-fruits.dom                         :as dom]
              [x.app-tools.temporary-component.config :as temporary-component.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-environment-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (DOM-element)
  []
  (dom/get-element-by-id temporary-component.config/ENVIRONMENT-ELEMENT-ID))

(defn create-temporary-container!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (DOM-element)
  []
  (let [environment-element (get-environment-element)
        temporary-container (dom/create-element! "DIV")]
       (dom/set-element-id! temporary-container "x-temporary-component")
       (dom/append-element! environment-element temporary-container)))

(defn remove-temporary-container!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [temporary-container (dom/get-element-by-id "x-temporary-component")]
          (let [environment-element (get-environment-element)]
               (dom/remove-child! environment-element temporary-container))))
