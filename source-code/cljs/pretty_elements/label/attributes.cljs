
(ns pretty-elements.label.attributes
    (:require [dom.api                            :as dom]
              [metamorphic-content.api            :as metamorphic-content]
              [pretty-build-kit.api               :as pretty-build-kit]
              [pretty-elements.core.side-effects  :as core.side-effects]
              [pretty-elements.label.side-effects :as label.side-effects]
              [pretty-elements.label.utils        :as label.utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-info-text-button-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :data-selectable (boolean)
  ;  :data-icon-family (keyword)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [label-id _]
  {:class             :pe-label--info-text-button
   :data-click-effect :opacity
   :data-selectable   false
   :data-icon-family  :material-symbols-outlined
   :data-icon-size    :xs
   :on-click    #(label.side-effects/toggle-info-text-visibility! label-id)
   :on-mouse-up #(dom/blur-active-element!)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copyable-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :data-tooltip-content (string)
  ;  :data-tooltip-position (keyword)
  ;  :on-click (function)
  [label-id label-props]
  {:class                 :pe-label--copyable
   :data-click-effect     :opacity
   :data-tooltip-position :right
   :data-tooltip-content  (metamorphic-content/compose :copy!)
   :on-click              (label.utils/on-copy-f label-id label-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ label-props]
  (-> {:class :pe-label--content}
      (pretty-build-kit/text-attributes label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-body-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [focus-id style] :as label-props}]
  (-> {:class               :pe-label--body
       :style               style
       :data-letter-spacing :auto
       :on-mouse-up #(if focus-id (core.side-effects/focus-element! focus-id))}
      (pretty-build-kit/border-attributes           label-props)
      (pretty-build-kit/color-attributes            label-props)
      (pretty-build-kit/font-attributes             label-props)
      (pretty-build-kit/element-min-size-attributes label-props)
      (pretty-build-kit/element-size-attributes     label-props)
      (pretty-build-kit/indent-attributes           label-props)
      (pretty-build-kit/marker-attributes           label-props)
      (pretty-build-kit/row-attributes              label-props)
      (pretty-build-kit/text-attributes             label-props)
      (pretty-build-kit/tooltip-attributes          label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {}
  [_ label-props]
  (-> {:class :pe-label}
      (pretty-build-kit/class-attributes        label-props)
      (pretty-build-kit/outdent-attributes      label-props)
      (pretty-build-kit/state-attributes        label-props)
      (pretty-build-kit/wrapper-size-attributes label-props)))
