
(ns pretty-elements.breadcrumbs.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
      (pretty-build-kit/color-attributes        crumb-props)
      (pretty-build-kit/cursor-attributes       crumb-props)
      (pretty-build-kit/effect-attributes       crumb-props)
      (pretty-build-kit/link-attributes         crumb-props)
      (pretty-build-kit/state-attributes        crumb-props)
      (pretty-build-kit/unselectable-attributes crumb-props)))

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
      (pretty-build-kit/indent-attributes breadcrumbs-props)
      (pretty-build-kit/style-attributes  breadcrumbs-props)))

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
      (pretty-build-kit/class-attributes   breadcrumbs-props)
      (pretty-build-kit/outdent-attributes breadcrumbs-props)
      (pretty-build-kit/state-attributes   breadcrumbs-props)))
