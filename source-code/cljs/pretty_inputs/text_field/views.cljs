
(ns pretty-inputs.text-field.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [fruits.vector.api                   :as vector]
              [metamorphic-content.api             :as metamorphic-content]
              [pretty-forms.api                    :as pretty-forms]
              [pretty-inputs.core.env              :as core.env]
              [pretty-inputs.core.side-effects :as core.side-effects]
              [pretty-inputs.core.views            :as core.views]
              [pretty-inputs.text-field.env       :as text-field.env]
              [pretty-inputs.text-field.attributes :as text-field.attributes]
              [pretty-inputs.text-field.prototypes :as text-field.prototypes]
              [pretty-presets.api                  :as pretty-presets]
              [reagent.api                         :as reagent]
              [time.api                            :as time]
              [countdown-timer.api :as countdown-timer]))

;; -- Field adornments components ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-adornment
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (map) adornment-props
  ; {:icon (keyword)(opt)
  ;  :label (string)(opt)
  ;  :on-click-f (function)(opt)}
  [field-id field-props adornment-props]
  ; - The render function ensures that the 'adornment-id' doesn't change even if the given parameters were updated.
  ; - The adornment icon must be in a separate I tag, otherwise the icon related data attributes would affect on the tooltip properties.
  (let [adornment-id (random/generate-keyword)]
       (fn [_ _ {:keys [icon label on-click-f] :as adornment-props}]
           (let [time-left       (countdown-timer/time-left adornment-id)
                 adornment-props (text-field.prototypes/adornment-props-prototype adornment-id adornment-props)]
                [(cond time-left :div on-click-f :button :else :div)
                 (cond time-left (text-field.attributes/countdown-adornment-attributes adornment-id adornment-props)
                       :default  (text-field.attributes/adornment-attributes           adornment-id adornment-props))
                 (cond time-left [:span (-> time-left time/ms->s (str "s"))]
                       icon      [:i    (text-field.attributes/adornment-icon-attributes adornment-id adornment-props) icon]
                       label     [:span (-> label metamorphic-content/compose)])]))))

(defn field-end-adornments
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:end-adornments (maps in vector)(opt)}
  [field-id {:keys [end-adornments] :as field-props}]
  (let [end-adornments (text-field.prototypes/end-adornments-prototype field-id field-props)]
       (if (vector/not-empty? end-adornments)
           (letfn [(f0 [adornment-props] (let [adornment-props (pretty-presets/apply-preset adornment-props)]
                                              [field-adornment field-id field-props adornment-props]))]
                  (hiccup/put-with [:div {:class :pi-text-field--adornments}] end-adornments f0))
           [:div (text-field.attributes/field-adornments-placeholder-attributes field-id field-props)])))

(defn field-start-adornments
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:start-adornments (maps in vector)(opt)}
  [field-id {:keys [start-adornments] :as field-props}]
  (if (vector/not-empty? start-adornments)
      (letfn [(f0 [adornment-props] [field-adornment field-id field-props adornment-props])]
             (hiccup/put-with [:div {:class :pi-text-field--adornments}] start-adornments f0))
      [:div (text-field.attributes/field-adornments-placeholder-attributes field-id field-props)]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  [field-id {:keys [multiline? placeholder surface] :as field-props}]
  ; - The placeholder element has an absolute position. Therefore, ...
  ;   ... it must be placed within the same ancestor element as the input element!
  ;   ... but it cannot be in the very same parent element as the input element
  ;       (otherwise, somehow it covers the input, regardless their order)!
  ; - The surface element is placed outside of the input container, otherwise
  ;   it would be misplaced in case the text-field has indent (applied on the input container).
  ; - The surface element has a relatively positioned wrapper element, otherwise
  ;   it wouldn't shrink (in terms of width) in case the text-field has outdent.
  [:div (text-field.attributes/field-attributes field-id field-props)
        [core.views/input-synchronizer field-id field-props]
        [core.views/input-label        field-id field-props]
        [:div (text-field.attributes/input-container-attributes field-id field-props)
              [field-start-adornments field-id field-props]
              [:div {:class :pi-text-field--input-structure}
                    (if placeholder (if-let [field-empty? (core.env/input-empty? field-id field-props)]
                                            [:div (text-field.attributes/field-placeholder-attributes field-id field-props)
                                                  (metamorphic-content/compose placeholder)]))
                    [:div (text-field.attributes/input-emphasize-attributes field-id field-props)
                          [(if multiline? :textarea :input)
                           (text-field.attributes/field-input-attributes field-id field-props)]]]
              [field-end-adornments field-id field-props]]
        (if surface (if (text-field.env/field-surface-visible? field-id field-props)
                        [:div {:class :pi-text-field--surface-wrapper}
                              [:div (text-field.attributes/field-surface-attributes field-id field-props)
                                    [metamorphic-content/compose surface]]]))
        (if-let [invalid-message (pretty-forms/get-input-invalid-message field-id)]
                [:div {:class :pi-text-field--invalid-message :data-text-selectable false}
                      (metamorphic-content/compose invalid-message)])])

(defn- text-field-lifecycles
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (core.side-effects/input-did-mount    field-id field-props))
                       :component-will-unmount (fn [_ _] (core.side-effects/input-will-unmount field-id field-props))
                       :reagent-render         (fn [_ field-props] [text-field field-id field-props])}))

(defn input
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ; {:autofill-name (keyword)(opt)
  ;  :autofocus? (boolean)(opt)
  ;  :border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :emptiable? (boolean)(opt)
  ;  :end-adornments (maps in vector)(opt)
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
  ;      Default: :default
  ;     :timeout (ms)(opt)
  ;     :tooltip-content (metamorphic-content)(opt)}]
  ;  :font-size (keyword, px or string)(opt)
  ;   Default: :s
  ;  :font-weight (keyword or integer)(opt)
  ;   Default: :normal
  ;  :form-id (keyword)(opt)
  ;  :get-value-f (function)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info-text (metamorphic-content)(opt)
  ;  :initial-value (string)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :line-height (keyword, px or string)(opt)
  ;   Default: :text-block
  ;  :marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)
  ;  :max-length (integer)(opt)
  ;  :modifier-f (function)(opt)
  ;  :on-blur-f (function)(opt)
  ;  :on-changed-f (function)(opt)
  ;  :on-empty-f (function)(opt)
  ;  :on-enter-f (function)(opt)
  ;  :on-focus-f (function)(opt)
  ;  :on-invalid-f (function)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-type-ended-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :on-valid-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :reveal-effect (keyword)(opt)
  ;  :set-value-f (function)(opt)
  ;  :start-adornments (maps in vector)(opt)
  ;  :style (map)(opt)
  ;  :surface (map)(opt)
  ;   {:border-radius (map)(opt)
  ;     {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;    :content (metamorphic-content)(opt)
  ;    :indent (map)(opt)
  ;    :placeholder (metamorphic-content)(opt)}
  ;  :type (keyword)(opt)
  ;   :email, :number, :password, :tel, :text
  ;   Default: :text
  ;  :validate-when-change? (boolean)(opt)
  ;  :validate-when-leave? (boolean)(opt)
  ;  :validators (maps in vector)(opt)
  ;   [{:f (function)
  ;     :invalid-message (metamorphic-content)(opt)}]
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [text-field {...}]
  ;
  ; @usage
  ; [text-field :my-text-field {...}]
  ;
  ; @usage
  ; (defn my-surface [field-id])
  ; [text-field {:surface #'my-surface}]
  ;
  ; @usage
  ; (defn my-surface [field-id])
  ; [text-field {:surface {:content #'my-surface}}]
  ;
  ; @usage
  ; [text-field {:modifier-f clojure.string/lower-case}]
  ;
  ; @usage
  ; [text-field {:validators []}]
  ([field-props]
   [input (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [field-props (pretty-presets/apply-preset                          field-props)
             field-props (text-field.prototypes/field-props-prototype field-id field-props)]
            [text-field-lifecycles field-id field-props]))))
