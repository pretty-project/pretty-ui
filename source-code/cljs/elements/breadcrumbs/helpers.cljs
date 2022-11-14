
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.breadcrumbs.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [re-frame.api                  :as r]))



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
  [breadcrumbs-id _ {:keys [disabled? href on-click]}]
  (if disabled? {:disabled       true}
                {:data-clickable true
                 :href           href
                 :on-click       #(r/dispatch on-click)
                 :on-mouse-up    #(element.side-effects/blur-element! breadcrumbs-id)}))

(defn breadcrumbs-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [style]}]
  {:data-scrollable-x true
   :style             style})

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

(defn static-crumb-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;  {}
  ; @param (map) crumb
  ;
  ; @return (map)
  ;  {}
  [_ _ {:keys []}]
  {:data-selectable false})

(defn button-crumb-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;  {}
  ; @param (map) crumb
  ;
  ; @return (map)
  ;  {}
  [breadcrumbs-id _ {:keys [route]}]
  {:data-clickable  true
   :data-selectable false
   :on-click    #(r/dispatch [:x.router/go-to! route])
   :on-mouse-up #(element.side-effects/blur-element! breadcrumbs-id)})
