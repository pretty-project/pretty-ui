
(ns elements.label.attributes
    (:require [elements.label.helpers :as label.helpers]
              [pretty-css.api         :as pretty-css]
              [x.components.api       :as x.components]
              [x.environment.api      :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-info-text-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  {:class             :e-label--info-text-button
   :data-click-effect :opacity
   :data-selectable   false
   :data-icon-family  :material-symbols-outlined
   :data-icon-size    :xs
   :on-click    #(label.helpers/toggle-info-text-visiblity! label-id)
   :on-mouse-up #(x.environment/blur-element!               label-id)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copyable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  {:class                 :e-label--copyable
   :data-click-effect     :opacity
   :data-tooltip-position :right
   :data-tooltip-content  (x.components/content :copy!)
   :on-click              (label.helpers/on-copy-f label-id label-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:target-id (keyword)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :for (string)}
  [_ {:keys [target-id] :as label-props}]
  (-> {:class :e-label--content
       :for   target-id}
      (pretty-css/text-attributes label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [gap horizontal-align horizontal-position style vertical-position] :as label-props}]
  (-> {:class                     :e-label--body
       :data-column-gap           gap
       :data-horizontal-position  horizontal-position
       :data-horizontal-row-align horizontal-align
       :data-vertical-position    vertical-position
       :style                     style
       :data-letter-spacing       :auto}
      (pretty-css/border-attributes           label-props)
      (pretty-css/color-attributes            label-props)
      (pretty-css/element-min-size-attributes label-props)
      (pretty-css/font-attributes             label-props)
      (pretty-css/indent-attributes           label-props)
      (pretty-css/marker-attributes           label-props)
      (pretty-css/text-attributes             label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {}
  [_ label-props]
  (-> {:class :e-label}
      (pretty-css/default-attributes label-props)
      (pretty-css/outdent-attributes label-props)))
