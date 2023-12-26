
(ns pretty-elements.text-field.views
    (:require [fruits.hiccup.api                     :as hiccup]
              [fruits.random.api                     :as random]
              [fruits.vector.api                     :as vector]
              [metamorphic-content.api               :as metamorphic-content]
              [pretty-elements.element.views         :as element.views]
              [pretty-elements.form.state            :as form.state]
              [pretty-elements.plain-field.env       :as plain-field.env]
              [pretty-elements.plain-field.views     :as plain-field.views]
              [pretty-elements.text-field.attributes :as text-field.attributes]
              [pretty-elements.text-field.prototypes :as text-field.prototypes]
              [pretty-presets.api                    :as pretty-presets]
              [re-frame.api                          :as r]
              [reagent.api                           :as reagent]
              [time.api                              :as time]))

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
  ;  :on-click (function or Re-Frame metamorphic-event)(opt)
  ;  :timeout (ms)(opt)}
  [field-id field-props adornment-props]
  ; Local state for the countdown timer
  (let [time-left (reagent/ratom nil)]

       ; ...
       (fn [_ _ {:keys [icon label on-click timeout] :as adornment-props}]

           ; This function ('f0') controls the countdown timer loop
           (letfn [(f0 [] (if   (not= @time-left 0)    (time.api/set-timeout! f0 1000))
                          (cond (=    @time-left 0)    (reset! time-left nil)
                                (->   @time-left nil?) (reset! time-left timeout)
                                :decrease-time-left    (swap!  time-left - 1000))
                          (-> on-click))]

                  ; ...
                  (if @time-left ; ...
                                 (let [adornment-props (text-field.prototypes/adornment-props-prototype field-props adornment-props)
                                       adornment-props (dissoc adornment-props :click-effect :hover-effect :icon-family :icon-size)
                                       adornment-props (assoc  adornment-props :text-color :highlight)]
                                      [:div (text-field.attributes/adornment-attributes field-id field-props adornment-props)
                                            (-> @time-left time/ms->s (str "s"))])

                                 ; ...
                                 (let [adornment-props (assoc adornment-props :on-click (if timeout f0 on-click))
                                       adornment-props (text-field.prototypes/adornment-props-prototype field-props adornment-props)]
                                      [(if on-click :button :div)
                                       (text-field.attributes/adornment-attributes field-id field-props adornment-props)
                                       (or icon (metamorphic-content/compose label))]))))))

(defn field-end-adornments
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:end-adornments (maps in vector)(opt)}
  [field-id {:keys [end-adornments] :as field-props}]
  (let [end-adornments (text-field.prototypes/end-adornments-prototype field-id field-props)]
       (if (vector/nonempty? end-adornments)
           (letfn [(f0 [adornment-props] (let [adornment-props (pretty-presets/apply-preset adornment-props)]
                                              [field-adornment field-id field-props adornment-props]))]
                  (hiccup/put-with [:div {:class :pe-text-field--adornments}] end-adornments f0))
           [:div (text-field.attributes/field-adornments-placeholder-attributes field-id field-props)])))

(defn field-start-adornments
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:start-adornments (maps in vector)(opt)}
  [field-id {:keys [start-adornments] :as field-props}]
  (if (vector/nonempty? start-adornments)
      (letfn [(f0 [adornment-props] [field-adornment field-id field-props adornment-props])]
             (hiccup/put-with [:div {:class :pe-text-field--adornments}] start-adornments f0))
      [:div (text-field.attributes/field-adornments-placeholder-attributes field-id field-props)]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-field-structure
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  [field-id {:keys [multiline? placeholder surface] :as field-props}]
  ; The placeholder element has an absolute position. Therefore, ...
  ; ... it must be placed within the same ancestor element as the input element!
  ; ... but it cannot be in the very same parent element as the input element!
  ;     (otherwise, somehow it covers the input, regardless their order)
  [:div (text-field.attributes/field-attributes field-id field-props)
        ; ...
        [element.views/element-label field-id field-props]
        ; ...
        [:div (text-field.attributes/input-container-attributes field-id field-props)
              ; ...
              [field-start-adornments field-id field-props]
              ; ...
              [:div {:class :pe-text-field--input-structure}
                    ; ...
                    (if placeholder (if-let [field-empty? (plain-field.env/field-empty? field-id)]
                                            [:div (text-field.attributes/field-placeholder-attributes field-id field-props)
                                                  (metamorphic-content/compose placeholder)]))
                    ; ...
                    [:div (text-field.attributes/input-emphasize-attributes field-id field-props)
                          [(if multiline? :textarea :input)
                           (text-field.attributes/field-input-attributes field-id field-props)]]]
              ; ...
              [field-end-adornments field-id field-props]
              ; ...
              (if surface (if (plain-field.env/surface-visible? field-id)
                              [:div (text-field.attributes/field-surface-attributes field-id field-props)
                                    [metamorphic-content/compose surface]]))]
        ; ...
        (if-let [invalid-message (field-id @form.state/FORM-ERRORS)]
                [:div {:class :pe-text-field--invalid-content-message :data-selectable false}
                      (metamorphic-content/compose invalid-message)])
        ; ...
        [plain-field.views/plain-field-synchronizer field-id field-props]])

(defn- text-field
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch [:pretty-elements.text-field/field-did-mount    field-id field-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:pretty-elements.text-field/field-will-unmount field-id field-props]))
                       :reagent-render         (fn [_ field-props] [text-field-structure field-id field-props])}))

(defn element
  ; @info
  ; XXX#0711
  ; Some other items based on the 'text-field' element and their documentations link here.
  ;
  ; @description
  ; The 'text-field' element writes its actual value into the Re-Frame state delayed,
  ; only when the user stopped typing or without a delay when the user leaves the field!
  ;
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ; {:autoclear? (boolean)(opt)
  ;   Removes the value stored in the application state (on the value-path)
  ;   when the element unmounts.
  ;  :autofill-name (keyword)(opt)
  ;   Helps the browser on what values to be suggested.
  ;   Leave empty if you don't want autosuggestions.
  ;  :autofocus? (boolean)(opt)
  ;  :border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:tl (keyword)(opt)
  ;    :tr (keyword)(opt)
  ;    :br (keyword)(opt)
  ;    :bl (keyword)(opt)
  ;    :all (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :border-width (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :emptiable? (boolean)(opt)
  ;  :end-adornments (maps in vector)(opt)
  ;   [{:click-effect (keyword)(opt)
  ;      :opacity
  ;      Default: :opacity
  ;     :disabled? (boolean)(opt)
  ;     :hover-effect (keyword)(opt)
  ;      :opacity
  ;     :icon (keyword)
  ;     :icon-family (keyword)(opt)
  ;      :material-symbols-filled, :material-symbols-outlined
  ;      Default: :material-symbols-outlined
  ;     :label (string)(opt)
  ;     :on-click (function or Re-Frame metamorphic-event)(opt)
  ;     :preset (keyword)(opt)
  ;     :tab-indexed? (boolean)(opt)
  ;      Default: true
  ;     :text-color (keyword)(opt)
  ;      :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;      Default: :default
  ;     :timeout (ms)(opt)
  ;      Disables the adornment for a specific time after the on-click event fired.
  ;     :tooltip-content (metamorphic-content)(opt)}]
  ;  :field-content-f (function)(opt)
  ;   From application state to field content modifier function.
  ;   Default: return
  ;  :field-value-f (function)(opt)
  ;   From field content to application state modifier function.
  ;   Default: return
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :font-weight (keyword)(opt)
  ;   :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  ;   Default: :normal
  ;  :form-id (keyword)(opt)
  ;   Different inputs with the same form ID could be validated at the same time.
  ;  :height (keyword, px or string)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :info-text (metamorphic-content)(opt)
  ;  :initial-value (string)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :line-height (keyword or px)(opt)
  ;   :auto, :inherit, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :text-block
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :marker-position (keyword)(opt)
  ;   :tl, :tr, :br, :bl, left, :right, bottom, :top
  ;  :max-length (integer)(opt)
  ;  :modifier-f (function)(opt)
  ;  :on-blur (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter.
  ;  :on-changed (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter.
  ;   It happens BEFORE the application state gets updated with the actual value!
  ;   If you want to get the ACTUAL value read from the application state, use the
  ;   :on-type-ended event instead!
  ;  :on-empty (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter.
  ;  :on-enter (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter.
  ;  :on-focus (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter.
  ;  :on-invalid (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content and the invalid message as its last parameter.
  ;  :on-mount (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter.
  ;  :on-type-ended (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter.
  ;  :on-unmount (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter.
  ;  :on-valid (Re-Frame metamorphic-event)(opt)
  ;   This event takes the field content as its last parameter.
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :reveal-effect (keyword)(opt)
  ;   :delayed, :opacity
  ;  :start-adornments (maps in vector)(opt)
  ;   Same as the :end-adornments property.
  ;  :style (map)(opt)
  ;  :surface (map)(opt)
  ;   {:border-radius (map)(opt)
  ;    :content (metamorphic-content)(opt)
  ;    :indent (map)(opt)}
  ;  :type (keyword)(opt)
  ;   :email, :number, :password, :tel, :text
  ;   Default: :text
  ;  :validate-when-change? (boolean)(opt)
  ;   Validates the value when it changes.
  ;  :validate-when-leave? (boolean)(opt)
  ;   Validates the value and turns on the validation when the user leaves the field.
  ;  :validators (maps in vector)(opt)
  ;  [{:f (function)
  ;    :invalid-message (metamorphic-content)(opt)}]
  ;  :value-path (Re-Frame path vector)(opt)
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
   [element (random/generate-keyword) field-props])

  ([field-id field-props]
   ; @note (tutorials#parametering)
   (fn [_ field-props]
       (let [field-props (pretty-presets/apply-preset                          field-props)
             field-props (text-field.prototypes/field-props-prototype field-id field-props)]
            [text-field field-id field-props]))))
