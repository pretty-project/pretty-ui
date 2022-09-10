
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.breadcrumbs.helpers
    (:require [x.app-core.api                 :as a]
              [x.app-elements.element.helpers :as element.helpers]
              [x.app-environment.api          :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-item-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;  {}
  ; @param (map) item-props
  ;
  ; @return (map)
  ;  {}
  [_ _ {:keys [disabled? href on-click]}]
  (if disabled? {:disabled       true}
                {:data-clickable true
                 :href           href
                 :on-click       #(a/dispatch on-click)
                 :on-mouse-up    #(environment/blur-element!)}))

(defn breadcrumbs-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [breadcrumbs-id {:keys [] :as breadcrumbs-props}]
  (merge (element.helpers/element-default-attributes breadcrumbs-id breadcrumbs-props)
         (element.helpers/element-indent-attributes  breadcrumbs-id breadcrumbs-props)
         {}))
