
(ns pretty-elements.breadcrumbs.attributes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-attributes
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; @param (map) crumb-props
  ; {:disabled? (boolean)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :disabled (boolean)
  ;  :data-font-size (keyword)
  ;  :data-font-weight (keyword)
  ;  :data-letter-spacing (keyword)
  ;  :data-line-height (keyword)
  ;  :data-text-overflow (keyword)}
  [_ _ {:keys [disabled?] :as crumb-props}]
  (-> {:class               :pe-breadcrumbs--crumb
       :data-font-size      :xs
       :data-font-weight    :semi-bold
       :data-letter-spacing :auto
       :data-line-height    :text-block
       :data-text-overflow  :ellipsis
       :disabled            disabled?}
      (pretty-css/color-attributes             crumb-props)
      (pretty-css/cursor-attributes            crumb-props)
      (pretty-css/effect-attributes            crumb-props)
      (pretty-css/href-attributes              crumb-props)
      (pretty-css/state-attributes             crumb-props)
      (pretty-css/unselectable-text-attributes crumb-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-body-attributes
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-column-gap (keyword)
  ;  :data-scroll-axis (boolean)}
  [_ breadcrumbs-props]
  (-> {:class            :pe-breadcrumbs--body
       :data-column-gap  :xs
       :data-scroll-axis :x}
      (pretty-css/indent-attributes breadcrumbs-props)
      (pretty-css/style-attributes  breadcrumbs-props)))

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
      (pretty-css/class-attributes   breadcrumbs-props)
      (pretty-css/outdent-attributes breadcrumbs-props)
      (pretty-css/state-attributes   breadcrumbs-props)))
