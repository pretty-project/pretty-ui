
(ns elements.select.views
    (:require [elements.button.views      :as button.views]
              [elements.element.views     :as element.views]
              [elements.icon-button.views :as icon-button.views]
              [elements.input.env         :as input.env]
              [elements.select.attributes :as select.attributes]
              [elements.select.env        :as select.env]
              [elements.select.prototypes :as select.prototypes]
              [elements.text-field.views  :as text-field.views]
              [hiccup.api                 :as hiccup]
              [layouts.api                :as layouts]
              [metamorphic-content.api    :as metamorphic-content]
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
  (let [on-click     {:fx [:elements.input/render-popup! select-id select-props]}
        button-label (select.env/select-button-label select-id select-props)]
       [:div (select.attributes/select-button-attributes select-id select-props)
             [element.views/element-label select-id select-props]
             [button.views/element select-id (assoc select-props :class         nil
                                                                 :gap           :auto
                                                                 :icon          :unfold_more
                                                                 :icon-position :right
                                                                 :label         button-label
                                                                 :on-click      on-click
                                                                 :outdent       nil)]]))

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
  ; XXX#0714 (source-code/cljs/elements/button/views.cljs)
  ; The 'select' element is based on the 'button' element.
  ; For more information check out the documentation of the 'button' element.
  ;
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ; {:add-option-f (function)(opt)
  ;   Default: return
  ;  :autoclear? (boolean)(opt)
  ;   Removes the value stored in the application state (on the value-path)
  ;   when the element unmounts.
  ;  :extendable? (boolean)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :info-text (metamorphic-content)(opt)
  ;  :initial-options (vector)(opt)
  ;  :initial-value (*)(opt)
  ;  :label (metamorphic-content)(opt)
  ;   Label of the button if the :layout property is :button or :icon-button,
  ;   otherwise it is displayed as the element label above the select button.
  ;  :layout (keyword)(opt)
  ;   :button, :icon-button, :select-button
  ;   Default: :select-button
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
