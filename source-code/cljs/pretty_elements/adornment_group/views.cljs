
(ns pretty-elements.adornment-group.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [fruits.vector.api                   :as vector]
              [metamorphic-content.api             :as metamorphic-content]
              [pretty-elements.adornment-group.attributes :as adornment-group.attributes]
              [pretty-elements.adornment-group.prototypes :as adornment-group.prototypes]
              [pretty-presets.api                  :as pretty-presets]
              [time.api                            :as time]
              [countdown-timer.api :as countdown-timer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (map) adornment-props
  ; {:icon (keyword)(opt)
  ;  :label (string)(opt)
  ;  :on-click-f (function)(opt)}
  [group-id group-props adornment-props]
  ; - The render function ensures that the 'adornment-id' doesn't change even if the given parameters were updated.
  ; - The adornment icon must be in a separate I tag, otherwise the icon related data attributes would affect on the tooltip properties.
  (let [adornment-id (random/generate-keyword)]
       (fn [_ _ {:keys [icon label on-click-f] :as adornment-props}]
           (let [time-left       (countdown-timer/time-left adornment-id)
                 adornment-props (adornment-group.prototypes/adornment-props-prototype adornment-id adornment-props)]
                [(cond time-left :div on-click-f :button :else :div)
                 (cond time-left (adornment-group.attributes/countdown-adornment-attributes adornment-id adornment-props)
                       :default  (adornment-group.attributes/adornment-attributes           adornment-id adornment-props))
                 (cond time-left [:div (adornment-group.attributes/adornment-label-attributes adornment-id adornment-props) (-> time-left time/ms->s (str "s"))]
                       icon      [:i   (adornment-group.attributes/adornment-icon-attributes  adornment-id adornment-props) (-> icon)]
                       label     [:div (adornment-group.attributes/adornment-label-attributes adornment-id adornment-props) (-> label metamorphic-content/compose)])]))))

(defn adornment-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:adornments (maps in vector)(opt)}
  [group-id {:keys [adornments] :as group-props}]
  [:div {:class :pe-adornment-group}
        [:div {:class :pe-adornment-group--body}
              (if (vector/not-empty? adornments)
                  (letfn [(f0 [%] [adornment group-id group-props %])]
                         (hiccup/put-with [:div {:class :pe-adornment-group--adornments}] adornments f0)))]])

(defn element
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; {:adornments (maps in vector)(opt)
  ;   [{:click-effect (keyword)(opt)
  ;      Default: :opacity
  ;     :disabled? (boolean)(opt)
  ;     :hover-effect (keyword)(opt)
  ;     :icon (keyword)
  ;     :icon-family (keyword)(opt)
  ;     :label (string)(opt)
  ;     :on-click-f (function)(opt)
  ;     :preset (keyword)(opt)
  ;     :tab-disabled? (boolean)(opt)
  ;     :text-color (keyword or string)(opt)
  ;     :timeout (ms)(opt)
  ;     :tooltip-content (metamorphic-content)(opt)}]
  ;  :border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [adornment-group {...}]
  ;
  ; @usage
  ; [adornment-group :my-adornment-group {...}]
  ([group-props]
   [element (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parametering)
   (fn [_ group-props]
       (let [group-props (pretty-presets/apply-preset group-props)]
             ; group-props (adornment-group.prototypes/group-props-prototype group-props)
            [adornment-group group-id group-props]))))
