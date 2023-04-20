
(ns elements.select.views
    (:require [elements.button.views      :as button.views]
              [elements.icon-button.views :as icon-button.views]
              [elements.input.env         :as input.env]
              [elements.select.attributes :as select.attributes]
              [elements.select.env        :as select.env]
              [elements.select.prototypes :as select.prototypes]
              [elements.text-field.views  :as text-field.views]
              [hiccup.api                 :as hiccup]
              [layouts.api                :as layouts]
              [metamorphic-content.api    :as metamorphic-content]
              [pretty-css.api             :as pretty-css]
              [random.api                 :as random]
              [re-frame.api               :as r]
              [reagent.api                :as reagent]
              [vector.api                 :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- option-field
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:extendable? (boolean)(opt)
  ;  :option-field-placeholder (metamorphic-content)}
  [select-id {:keys [extendable? option-field-placeholder] :as select-props}]
  (if extendable? (let [adornment-on-click [:elements.select/add-option! select-id select-props]
                        adornment-props    {:icon :add :on-click adornment-on-click :title :add!}]
                       [text-field.views/element :elements.select/option-field
                                                 {:end-adornments [adornment-props]
                                                  :indent         {:bottom :xs :vertical :xs}
                                                  :placeholder    option-field-placeholder}])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-option-list-items
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id {:keys [option-label-f] :as select-props}]
  (let [options (input.env/get-input-options select-id select-props)]
       (letfn [(f [option] (if (select.env/render-option? select-id select-props option)
                               [:button (select.attributes/select-option-attributes select-id select-props option)
                                        (-> option option-label-f metamorphic-content/compose)]))]
              (hiccup/put-with [:<>] options f))))

(defn- select-option-list
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  [select-id {:keys [options-placeholder] :as select-props}]
  (let [options (input.env/get-input-options select-id select-props)]
       [:div {:class :e-select--option-list :data-selectable false}
             (if (vector/nonempty? options)
                 [select-option-list-items select-id select-props]
                 [:div (select.attributes/select-options-placeholder-attributes select-id select-props)
                       (metamorphic-content/compose options-placeholder)])]))

(defn- select-options-body
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (reagent/lifecycles {:reagent-render         (fn [_ _] [select-option-list select-id select-props])
                       :component-did-mount    (fn [_ _] (r/dispatch [:elements.select/select-options-did-mount    select-id select-props]))
                       :component-will-unmount (fn [_ _] (r/dispatch [:elements.select/select-options-will-unmount select-id select-props]))}))

(defn- select-options-header
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:popup (map)(opt)
  ;   {:label (metamorphic-content)(opt)}}
  [select-id {{:keys [label]} :popup :as select-props}]
  [:div {:class :e-select--options--header :data-selectable false}
        [:div (select.attributes/select-options-label-attributes select-id select-props)
              (metamorphic-content/compose label)]
        [option-field select-id select-props]])

(defn- select-options
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:popup (map)(opt)}
  [select-id {:keys [popup] :as select-props}]
  (if (input.env/popup-rendered? select-id)
      [:div {:class :e-select--options}
            [layouts/struct-popup :elements.select/options
                                  (assoc popup :body     [select-options-body   select-id select-props]
                                               :header   [select-options-header select-id select-props]
                                               :on-cover {:fx [:elements.input/close-popup! select-id select-props]})]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-button
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [on-click {:fx [:elements.input/render-popup! select-id select-props]}
        label    (select.env/select-button-label select-id select-props)]
       [:div (pretty-css/effect-attributes {:class :e-select-button} select-props)
             [button.views/element select-id (assoc select-props :class         :e-select-button
                                                                 :icon          :unfold_more
                                                                 :icon-position :right
                                                                 :label         label
                                                                 :on-click      on-click)]]))

(defn- select-button-layout
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:elements.select/select-button-did-mount select-id select-props]))
                       :reagent-render      (fn [_ select-props] [select-button select-id select-props])}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-button-layout
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [on-click {:fx [:elements.input/render-popup! select-id select-props]}]
       [icon-button.views/element select-id (assoc select-props :on-click on-click)]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-layout
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [on-click {:fx [:elements.input/render-popup! select-id select-props]}]
       [button.views/element select-id (assoc select-props :on-click on-click)]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:layout (keyword)}
  [select-id {:keys [layout] :as select-props}]
  [:<> (case layout :button        [button-layout        select-id select-props]
                    :icon-button   [icon-button-layout   select-id select-props]
                    :select-button [select-button-layout select-id select-props])
       [select-options select-id select-props]])

(defn element
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ; {:add-option-f (function)(opt)
  ;   Default: return
  ;  :autoclear? (boolean)(opt)
  ;  :border-color (keyword)(opt)
  ;   :default, :highlight, :invert, :primary, :secondary, :success, :transparent, :warning
  ;  :border-position (keyword)(opt)
  ;   :all, :bottom, :top, :left, :right, :horizontal, :vertical
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
  ;  :extendable? (boolean)(opt)
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
  ;  :initial-options (vector)(opt)
  ;  :initial-value (*)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :layout (keyword)(opt)
  ;   :button, :icon-button, :select-button
  ;   Default: :select-button
  ;  :marker-color (keyword)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :on-select (Re-Frame metamorphic-event)(opt)
  ;  :option-field-placeholder (metamorphic-content)(opt)
  ;   Default: :add!
  ;  :option-label-f (function)(opt)
  ;   Default: return
  ;  :option-value-f (function)(opt)
  ;   Default: return
  ;  :options (vector)(opt)
  ;  :options-label (metamorphic-content)(opt)
  ;  :options-path (Re-Frame path vector)(opt)
  ;  :options-placeholder (metamorphic-content)(opt)
  ;   Default: :no-options
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :popup (map)(opt)
  ;   {:border-color (keyword or string)(opt)
  ;    :border-position (keyword)(opt)
  ;    :border-radius (map)(opt)
  ;    :border-width (keyword)(opt)
  ;    :cover-color (keyword or string)(opt)
  ;     Default: :black
  ;    :fill-color (keyword or string)(opt)
  ;     Default: :default
  ;    :indent (map)(opt)
  ;    :label (metamorphic-content)(opt)
  ;    :min-width (keyword)(opt)
  ;    :outdent (map)(opt)}
  ;  :reveal-effect (keyword)(opt)
  ;   :delayed, :opacity
  ;  :style (map)(opt)
  ;  :value-path (Re-Frame path vector)(opt)}
  ;
  ; @usage
  ; [select {...}]
  ;
  ; @usage
  ; [select :my-select {...}]
  ;
  ; @usage
  ; [select {:icon         :sort
  ;          :layout       :icon-button
  ;          :options-path [:my-options]
  ;          :value-path   [:my-selected-option]}]
  ([select-props]
   [element (random/generate-keyword) select-props])

  ([select-id select-props]
   (let [select-props (select.prototypes/select-props-prototype select-id select-props)]
        [select select-id select-props])))
