
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.switch
    (:require [mid-fruits.candy          :refer [param]]
              [reagent.api               :as reagent]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]

              [x.app-elements.engine.element]
              [x.app-elements.engine.input]
              [x.app-elements.engine.form]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- switch-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;
  ; @return (map)
  ;  {:border-color (keyword)
  ;   :font-size (keyword)
  ;   :layout (keyword)}
  [switch-id switch-props]
  (merge {:border-color :primary
          :font-size    :s
          :layout       :row
          :value-path (engine/default-value-path switch-id)}
         (param switch-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-switch-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ;
  ; @return (map)
  [db [_ switch-id]]
  (merge (r engine/get-element-props   db switch-id)
         (r engine/get-checkable-props db switch-id)))

(a/reg-sub :elements/get-switch-props get-switch-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- switch-secondary-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {:secondary-label (metamorphic-content)}
  [_ {:keys [secondary-label]}]
  [:div.x-switch--secondary-label [components/content secondary-label]])

(defn- switch-secondary-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  [:button.x-switch--secondary-body (engine/checkable-secondary-body-attributes switch-id switch-props)
                                    [switch-secondary-label                     switch-id switch-props]])

(defn- switch-primary-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {:label (metamorphic-content)
  ;   :required? (boolean)(opt)}
  [_ {:keys [label required?]}]
  [:div.x-switch--primary-label [components/content label]
                                (if required? [:span.x-input--label-asterisk "*"])])

(defn- switch-primary-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  [:button.x-switch--primary-body (engine/checkable-primary-body-attributes switch-id switch-props)
                                  [:div.x-switch--track [:div.x-switch--thumb]]
                                  [switch-primary-label switch-id switch-props]])

(defn- switch-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {:label (metamorphic-content)
  ;   :required? (boolean)(opt)}
  [_ {:keys [label required?]}]
  [:div.x-switch--label [components/content label]
                        (if required? [:span.x-input--label-asterisk "*"])])

(defn- switch-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; (keyword) switch-id
  ; @param (map) switch-props
  [switch-id switch-props]
  [:button.x-switch--body (engine/checkable-body-attributes switch-id switch-props)
                          [:div.x-switch--track [:div.x-switch--thumb]]
                          [switch-label switch-id switch-props]])

(defn- switch
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ;  {:secondary-label (metamorphic-content)(opt)}
  [switch-id {:keys [secondary-label] :as switch-props}]
  [:div.x-switch (engine/checkable-attributes switch-id switch-props)
                 (if secondary-label [:div {:style {:display :flex}}
                                           [switch-secondary-body switch-id switch-props]
                                           [switch-primary-body   switch-id switch-props]]
                                     [switch-body switch-id switch-props])
                 [engine/element-helper       switch-id switch-props]
                 [engine/element-info-tooltip switch-id switch-props]])



(defn switch-did-mount
  [db [_ switch-id switch-props]]
  (let [switch-props (select-keys switch-props [:form-id :initial-value :value-path])]
       (as-> db % (r x.app-elements.engine.element/store-element-props! % switch-id switch-props)
                  (r x.app-elements.engine.input/use-initial-value!     % switch-id)
                  (r x.app-elements.engine.form/reg-form-input!         % switch-id))))

(a/reg-event-db :elements/switch-did-mount switch-did-mount)

(defn switch-will-unmount
  [db [_ switch-id]]
  (as-> db % (r x.app-elements.engine.form/remove-form-input!       % switch-id)
             (r x.app-elements.engine.element/remove-element-props! % switch-id)))

(a/reg-event-db :elements/switch-will-unmount switch-will-unmount)



(defn element
  ; @param (keyword)(opt) switch-id
  ; @param (map) switch-props
  ;  {:border-color (keyword)(opt)
  ;    :default, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;   :class (keyword or keywords in vector)(opt)
  ;   :default-value (boolean)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xs, :s
  ;    Default: :s
  ;   :form-id (keyword)(opt)
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :initial-value (boolean)(constant)(opt)
  ;   :label (metamorphic-content)
  ;   :secondary-label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-check (metamorphic-event)(constant)(opt)
  ;   :on-uncheck (metamorphic-event)(constant)(opt)
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :value-path (vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/switch {...}]
  ;
  ; @usage
  ;  [elements/switch :my-switch {...}]
  ([switch-props]
   [element (a/id) switch-props])

  ([switch-id {:keys [form-id initial-value] :as switch-props}]
   (let [switch-props (switch-props-prototype switch-id switch-props)]
        (reagent/lifecycles {:reagent-render         (fn [] [switch switch-id switch-props])
                             :component-did-mount    (fn [] (if (or form-id initial-value)
                                                                (a/dispatch [:elements/switch-did-mount switch-id switch-props])))
                             :component-will-unmount (fn [] (if (or form-id initial-value)
                                                                (a/dispatch [:elements/switch-will-unmount switch-id])))})

        [engine/stated-element switch-id
                               {:render-f      #'switch
                                :element-props switch-props
                                :initializer   [:elements/init-input!      switch-id]
                                :subscriber    [:elements/get-switch-props switch-id]}])))
