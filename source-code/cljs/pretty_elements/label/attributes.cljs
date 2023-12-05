
(ns pretty-elements.label.attributes
    (:require [dom.api                            :as dom]
              [metamorphic-content.api            :as metamorphic-content]
              [pretty-css.api                     :as pretty-css]
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
   :on-click    #(label.side-effects/toggle-info-text-visiblity! label-id)
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
  ; {:target-id (keyword)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :for (string)}
  [_ {:keys [target-id] :as label-props}]
  (-> {:class :pe-label--content
       :for   target-id}
      (pretty-css/text-attributes label-props)))

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
  [_ {:keys [gap horizontal-align horizontal-position style vertical-position] :as label-props}]
  (-> {:class                      :pe-label--body
       :data-column-gap            gap
       :data-horizontal-row-align  horizontal-position
       :data-horizontal-text-align horizontal-align
       :data-vertical-position     vertical-position
       :style                      style
       :data-letter-spacing        :auto}
      (pretty-css/border-attributes  label-props)
      (pretty-css/color-attributes   label-props)
      (pretty-css/font-attributes    label-props)
      (pretty-css/indent-attributes  label-props)
      (pretty-css/marker-attributes  label-props)
      (pretty-css/text-attributes    label-props)
      (pretty-css/tooltip-attributes label-props)))

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
      (pretty-css/default-attributes          label-props)
      (pretty-css/outdent-attributes          label-props)
      (pretty-css/element-min-size-attributes label-props)
      (pretty-css/element-size-attributes     label-props)))
