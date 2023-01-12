
(ns elements.breadcrumbs.helpers
    (:require [elements.element.helpers :as element.helpers]
              [re-frame.api             :as r]
              [x.environment.api        :as x.environment]))

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
  ; {:data-color (keyword)
  ;  :data-font-size (keyword)
  ;  :data-line-height (keyword)
  ;  :data-selectable (boolean)
  ;  :data-text-overflow (keyword)}
  [_ _ _]
  {:data-color         :muted
   :data-font-size     :xs
   :data-line-height   :text-block
   :data-selectable    false
   :data-text-overflow :ellipsis})

(defn button-crumb-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb
  ; {:route (string)}
  ;
  ; @return (map)
  ; {:data-click-effect (boolean)
  ;  :data-color (keyword)
  ;  :data-font-size (keyword)
  ;  :data-font-weight (keyword)
  ;  :data-line-height (keyword)
  ;  :data-selectable (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)
  ;  :data-text-overflow (keyword)}
  [breadcrumbs-id _ {:keys [route]}]
  {:data-click-effect  :opacity
   :data-color         :muted
   :data-font-size     :xs
   :data-font-weight   :bold
   :data-line-height   :text-block
   :data-selectable    false
   :data-text-overflow :ellipsis
   :on-click    #(r/dispatch [:x.router/go-to! route])
   :on-mouse-up #(x.environment/blur-element! breadcrumbs-id)})

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
  ; {:data-column-gap (keyword)
  ;  :data-scrollable-x (boolean)
  ;  :style (map)}
  [breadcrumbs-id {:keys [style] :as breadcrumbs-props}]
  (merge (element.helpers/element-indent-attributes breadcrumbs-id breadcrumbs-props)
         {:data-column-gap   :xs
          :data-scrollable-x true
          :style             style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
