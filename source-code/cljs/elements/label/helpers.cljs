
(ns elements.label.helpers
    (:require [elements.label.state :as label.state]
              [pretty-css.api       :as pretty-css]
              [re-frame.api         :as r]
              [x.components.api     :as x.components]
              [x.environment.api    :as x.environment]))

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
   :data-icon-family  :material-symbols-outlined
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
  ; {:data-bubble-content (string)
  ;  :data-bubble-position (keyword)
  ;  :data-click-effect (keyword)
  ;  :data-selectable (boolean)
  ;  :on-click (function)
  [label-id label-props]
  {:data-bubble-position :right
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
  [_ {:keys [target-id] :as label-props}]
  (-> {:for target-id}
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
  [_ {:keys [horizontal-align horizontal-position min-width selectable? style vertical-position] :as label-props}]
  (-> {:data-horizontal-position  horizontal-position
       :data-horizontal-row-align horizontal-align
       :data-element-min-width    min-width
       :data-selectable           selectable?
       :data-vertical-position    vertical-position
       :style                     style}
      (pretty-css/color-attributes  label-props)
      (pretty-css/font-attributes   label-props)
      (pretty-css/indent-attributes label-props)
      (pretty-css/marker-attributes label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  [_ label-props]
  (-> {} (pretty-css/default-attributes label-props)
         (pretty-css/outdent-attributes label-props)))
