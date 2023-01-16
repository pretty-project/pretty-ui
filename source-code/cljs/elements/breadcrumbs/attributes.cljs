
(ns elements.breadcrumbs.attributes
    (:require [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))

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
  ; {:class (keyword or keywords in vector)
  ;  :data-color (keyword)
  ;  :data-font-size (keyword)
  ;  :data-line-height (keyword)
  ;  :data-selectable (boolean)
  ;  :data-text-overflow (keyword)}
  [_ _ _]
  {:class              :e-breadcrumbs--crumb
   :data-color         :muted
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
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (boolean)
  ;  :data-color (keyword)
  ;  :data-font-size (keyword)
  ;  :data-font-weight (keyword)
  ;  :data-line-height (keyword)
  ;  :data-selectable (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)
  ;  :data-text-overflow (keyword)}
  [breadcrumbs-id _ {:keys [route]}]
  {:class              :e-breadcrumbs--crumb
   :data-click-effect  :opacity
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
  ; {:class (keyword or keywords in vector)
  ;  :data-column-gap (keyword)
  ;  :data-scroll-axis (boolean)
  ;  :style (map)}
  [_ {:keys [style] :as breadcrumbs-props}]
  (-> {:class            :e-breadcrumbs--body
       :data-column-gap  :xs
       :data-scroll-axis :x
       :style            style}
      (pretty-css/indent-attributes breadcrumbs-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ breadcrumbs-props]
  (-> {:class :e-breadcrumbs}
      (pretty-css/default-attributes breadcrumbs-props)
      (pretty-css/outdent-attributes breadcrumbs-props)))
