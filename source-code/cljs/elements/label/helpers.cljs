
(ns elements.label.helpers
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [elements.label.state          :as label.state]
              [re-frame.api                  :as r]
              [x.components.api              :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-copy-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:content (string)(opt)}
  ;
  ; @return (function)
  [_ {:keys [content]}]
  ; XXX#7009 (source-code/cljs/elements/label/prototypes.cljs)
  (fn [_] (r/dispatch [:clipboard/copy-text! content])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn info-text-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ;
  ; @return (boolean)
  [label-id]
  (get @label.state/INFO-TEXT-VISIBILITY label-id))

(defn toggle-info-text-visiblity!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  [label-id]
  (swap! label.state/INFO-TEXT-VISIBILITY update label-id not))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copyable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:data-clickable (boolean)
  ;  :data-copy-label (string)
  ;  :data-copyable (boolean)
  ;  :data-selectable (boolean)
  ;  :on-click (function)
  [label-id label-props]
  {:data-clickable  true
   :data-copyable   true
   :data-copy-label (x.components/content :copy!)
   :on-click        (on-copy-f label-id label-props)})

(defn label-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:color (keyword or string)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [color style]}]
  (-> {:style style}
      (element.helpers/apply-color :color :data-color color)))

(defn label-font-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:font-size (keyword)
  ;  :font-weight (keyword)
  ;  :line-height (keyword)}
  ;
  ; @return (map)
  ; {:data-font-size (keyword)
  ;  :data-font-weight (keyword)
  ;  :data-line-height (keyword)}
  [_ {:keys [font-size font-weight line-height]}]
  {:data-font-size   font-size
   :data-font-weight font-weight
   :data-line-height line-height})

(defn label-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:horizontal-align (keyword)
  ;  :horizontal-position (keyword)(opt)
  ;  :min-width (keyword)(opt)
  ;  :overflow-direction (keyword)(opt)
  ;  :vertical-position (keyword)(opt)}
  ;
  ; @return (map)
  ; {:data-element-width (keyword)
  ;  :data-horizontal-position (keyword)
  ;  :data-horizontal-row-align (keyword)
  ;  :data-overflow-direction (keyword)
  ;  :data-vertical-position (keyword)}
  [_ {:keys [horizontal-align horizontal-position min-width overflow-direction vertical-position]}]
  {:data-horizontal-position  horizontal-position
   :data-horizontal-row-align horizontal-align
   :data-element-width        min-width
   :data-vertical-position    vertical-position
   :data-overflow-direction   overflow-direction})

(defn label-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:selectable? (boolean)(opt)}
  ;
  ; @return (map)
  ; {:data-selectable (boolean)}
  [label-id {:keys [selectable?] :as label-props}]
  (merge (element.helpers/element-indent-attributes label-id label-props)
         (element.helpers/element-marker-attributes label-id label-props)
         (label-style-attributes                    label-id label-props)
         (label-font-attributes                     label-id label-props)
         (label-layout-attributes                   label-id label-props)
         {:data-selectable selectable?}))

(defn label-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  [label-id label-props]
  (merge (element.helpers/element-default-attributes label-id label-props)
         (element.helpers/element-outdent-attributes label-id label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-icon-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:icon-color (keyword or string)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword)}
  ;
  ; @return (map)
  ; {:data-icon-family (keyword)
  ;  :data-icon-size (keyword)}
  [_ {:keys [icon-color icon-family icon-size]}]
  (-> {:data-icon-family icon-family
       :data-icon-size   icon-size}
      (element.helpers/apply-color :icon-color :data-icon-color icon-color)))

(defn label-info-text-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:data-clickable (boolean)
  ;  :data-selectable (boolean)
  ;  :data-icon-family (keyword)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [label-id _]
  {:data-clickable   true
   :data-selectable  false
   :data-icon-family :material-icons-filled
   :data-icon-size   :xs
   :on-click        #(toggle-info-text-visiblity!        label-id)
   :on-mouse-up     #(element.side-effects/blur-element! label-id)})
