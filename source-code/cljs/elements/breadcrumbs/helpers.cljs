
(ns elements.breadcrumbs.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [re-frame.api                  :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-scrollable-x (boolean)
  ;  :style (map)}
  [breadcrumbs-id {:keys [style] :as breadcrumbs-props}]
  (merge (element.helpers/element-indent-attributes breadcrumbs-id breadcrumbs-props)
         {:data-scrollable-x true
          :style             style}))

(defn breadcrumbs-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  [breadcrumbs-id breadcrumbs-props]
  (merge (element.helpers/element-default-attributes breadcrumbs-id breadcrumbs-props)
         (element.helpers/element-outdent-attributes breadcrumbs-id breadcrumbs-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn static-crumb-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ;
  ; @return (map)
  ; {:data-font-size (keyword)
  ;  :data-line-height (keyword)
  ;  :data-selectable (boolean)}
  [_ _ _]
  {:data-font-size   :xs
   :data-line-height :block
   :data-selectable  false})

(defn button-crumb-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ; {:route (string)}
  ;
  ; @return (map)
  ; {:data-clickable (boolean)
  ;  :data-font-size (keyword)
  ;  :data-line-height (keyword)
  ;  :data-selectable (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [breadcrumbs-id _ {:keys [route]}]
  {:data-clickable  true
   :data-font-size   :xs
   :data-line-height :block
   :data-selectable false
   :on-click    #(r/dispatch [:x.router/go-to! route])
   :on-mouse-up #(element.side-effects/blur-element! breadcrumbs-id)})
