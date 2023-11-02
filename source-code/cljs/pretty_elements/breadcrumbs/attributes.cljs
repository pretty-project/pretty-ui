
(ns pretty-elements.breadcrumbs.attributes
    (:require [dom.api        :as dom]
              [pretty-css.api :as pretty-css]
              [re-frame.api   :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-attributes
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-font-size (keyword)
  ;  :data-font-weight (keyword)
  ;  :data-letter-spacing (keyword)
  ;  :data-line-height (keyword)
  ;  :data-selectable (boolean)
  ;  :data-text-overflow (keyword)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [_ _ {:keys [on-click] :as crumb-props}]
  (-> {:class               :pe-breadcrumbs--crumb
       :data-font-size      :xs
       :data-font-weight    :semi-bold
       :data-letter-spacing :auto
       :data-line-height    :text-block
       :data-selectable     false
       :data-text-overflow  :ellipsis
       :on-click    #(r/dispatch on-click)
       :on-mouse-up #(dom/blur-active-element!)}
      (pretty-css/color-attributes  crumb-props)
      (pretty-css/effect-attributes crumb-props)
      (pretty-css/link-attributes   crumb-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-body-attributes
  ; @ignore
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
  (-> {:class            :pe-breadcrumbs--body
       :data-column-gap  :xs
       :data-scroll-axis :x
       :style            style}
      (pretty-css/indent-attributes breadcrumbs-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-attributes
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ breadcrumbs-props]
  (-> {:class :pe-breadcrumbs}
      (pretty-css/default-attributes breadcrumbs-props)
      (pretty-css/outdent-attributes breadcrumbs-props)))
