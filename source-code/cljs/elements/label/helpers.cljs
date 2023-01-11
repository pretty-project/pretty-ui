
(ns elements.label.helpers
    (:require [elements.element.helpers :as element.helpers]
              [elements.label.state     :as label.state]
              [re-frame.api             :as r]
              [x.components.api         :as x.components]
              [x.environment.api        :as x.environment]))

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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-info-text-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)
  ;  :data-selectable (boolean)
  ;  :data-icon-family (keyword)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [label-id _]
  {:data-click-effect :opacity
   :data-selectable   false
   :data-icon-family  :material-icons-filled
   :data-icon-size    :xs
   :on-click    #(toggle-info-text-visiblity! label-id)
   :on-mouse-up #(x.environment/blur-element! label-id)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copyable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:data-bubble-color (keyword)
  ;  :data-bubble-content (string)
  ;  :data-bubble-position (keyword)
  ;  :data-click-effect (keyword)
  ;  :data-selectable (boolean)
  ;  :on-click (function)
  [label-id label-props]
  {:data-bubble-color    :primary
   :data-bubble-position :right
   :data-click-effect    :opacity
   :data-bubble-content  (x.components/content :copy!)
   :on-click             (on-copy-f label-id label-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {:target-id (keyword)
  ;  :text-direction (keyword)(opt)
  ;  :text-overflow (keyword)(opt)}
  ;
  ; @return (map)
  ; {:data-text-direction (keyword)
  ;  :data-text-overflow (keyword)}
  [_ {:keys [target-id text-direction text-overflow]}]
  {:data-text-direction text-direction
   :data-text-overflow  text-overflow
   :for                 target-id})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  ;  :vertical-position (keyword)(opt)}
  ;
  ; @return (map)
  ; {:data-element-min-width (keyword)
  ;  :data-horizontal-position (keyword)
  ;  :data-horizontal-row-align (keyword)
  ;  :data-vertical-position (keyword)}
  [_ {:keys [horizontal-align horizontal-position min-width vertical-position]}]
  {:data-horizontal-position  horizontal-position
   :data-horizontal-row-align horizontal-align
   :data-element-min-width    min-width
   :data-vertical-position    vertical-position})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
