
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.select
    (:require [mid-fruits.candy                              :refer [param return]]
              [mid-fruits.vector                             :as vector]
              [x.app-components.api                          :as components]
              [x.app-core.api                                :as a :refer [r]]
              [x.app-elements.engine.api                     :as engine]
              [x.app-elements.element-components.button      :as button :rename {element button}]
              [x.app-elements.element-components.icon-button :as icon-button]
              [x.app-elements.element-components.label               :rename {element label}]
              [x.app-elements.element-components.horizontal-polarity :rename {element horizontal-polarity}]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  (ns my-namespace (:require [x.app-elements.api :as elements]))
;  [elements/select {...}]
;
; @usage
;  A [select-options] komponenst megjelenítő popup UI elemet esemény-alapon is
;  lehetséges megjeleníteni, az [:elements/render-select-options! ...]
;  esemény meghívásával.
;
;  (a/dispatch [:elements/render-select-options! {...}])



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def CLOSE-POPUP-DELAY 150)

; @constant (ms)
(def AUTOCLEAR-VALUE-DELAY 500)

; @constant (ms)
(def ON-POPUP-CLOSED-DELAY 350)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- on-select-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) options-props
  ;  {:autoclear? (boolean)(opt)
  ;   :on-popup-closed (metamorphic-event)(opt)
  ;   :on-select (metamorphic-event)(opt)
  ;   :value-path (vector)}
  ;
  ; @return (map)
  [select-id {:keys [autoclear? on-popup-closed on-select value-path]}]
  {:dispatch on-select
   :dispatch-later [{:ms CLOSE-POPUP-DELAY :dispatch [:ui/close-popup! :elements/select-options]}
                    (when (boolean autoclear?) ; XXX#0134
                          {:ms AUTOCLEAR-VALUE-DELAY :dispatch [:elements/clear-input-value! select-id]})
                    (when (some? on-popup-closed)
                          {:ms ON-POPUP-CLOSED-DELAY :dispatch on-popup-closed})]})

(defn- select-props->select-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) select-props
  ;  {:get-label-f (function)
  ;   :select-button-label (metamorphic-content)(opt)
  ;   :value (*)}
  ;
  ; @return (metamorphic-content)
  [{:keys [get-label-f select-button-label] :as select-props}]
  (if-let [selected-option (get select-props :value)]
          (get-label-f selected-option)
          (or select-button-label :select!)))

(defn- options-props->render-popup-header?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) options-props
  ;  {:options-label (metamorphic-content)(opt)
  ;   :user-close? (boolean)(opt)}
  ;
  ; @return (boolean)
  [{:keys [options-label user-close?]}]
  (or options-label user-close?))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A select-button elem {:on-click ...} eseménye kirendereli
  ; a select-options elemet tartalmazó popup UI elemet.
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:value-path (vector)}
  ;
  ; @return (map)
  ;  {:get-label-f (function)
  ;   :get-value-f (function)
  ;   :layout (keyword)
  ;   :value-path (vector)}
  [select-id select-props]
  (let ; BUG#1507
       ; Ha a select-button elem {:disabled? true} állapotban csatolódik a React-fába,
       ; akkor a {:disabled? true} tulajdonságát az options-props térképben továbbörökítené
       ; az {:on-click [:elements/render-select-options! ...]} konstans tulajdonságon keresztül
       ; select-options elemnek.
       [options-props (dissoc select-props :disabled?)]
       (merge {:get-label-f  return
               :get-value-f  return
               :layout       :select
               :options-path (engine/default-options-path select-id)
               :value-path   (engine/default-value-path   select-id)}
              (param select-props)
              {:on-click [:elements/render-select-options! select-id options-props]})))

(defn- options-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) options-props
  ;  {:options-label (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ;  {:get-label-f (function)
  ;   :no-options-label (metamorphic-content)
  ;   :on-select (metamorphic-event)
  ;   :options-id (keyword)
  ;   :options-path (vector)
  ;   :value-path (vector)}
  [select-id {:keys [options-label] :as options-props}]
  (let [on-select (on-select-events select-id options-props)]
       (merge {:get-label-f      return
               :get-value-f      return
               :no-options-label :no-options
               :options-path (engine/default-options-path select-id)
               :value-path   (engine/default-value-path   select-id)}
              (param options-props)
              {:on-select  on-select
               :options-id (engine/element-id->extended-id select-id :options)})))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-select-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;  select-id or options-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (merge (r engine/get-element-props    db element-id)
         (r engine/get-selectable-props db element-id)))

(a/reg-sub :elements/get-select-props get-select-props)



;; -- Select options components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-options-close-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) options-id
  ; @param (map) options-props
  [options-id _]
  [button {:preset   :close-icon-button
           :on-click [:ui/close-popup! :elements/select-options]}])

(defn- select-options-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) options-id
  ; @param (map) options-props
  [_ {:keys [options-label]}]
  [label {:content options-label
          :indent  {:horizontal :xs}}])

(defn- select-options-label-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) options-id
  ; @param (map) options-props
  [options-id options-props]
  [horizontal-polarity ::select-options-label-header
                       {:middle-content [select-options-label options-id options-props]}])

(defn- select-options-cancel-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) options-id
  ; @param (map) options-props
  [options-id options-props]
  [horizontal-polarity ::select-options-cancel-header
                       {:end-content [select-options-close-button options-id options-props]}])

(defn- select-options-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) options-id
  ; @param (map) options-props
  [options-id {:keys [options-label user-cancel?] :as options-props}]
  (cond (some?   options-label) [select-options-label-header  options-id options-props]
        (boolean user-cancel?)  [select-options-cancel-header options-id options-props]))

(defn- select-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) options-id
  ; @param (map) options-props
  ;  {:get-label-f (function)
  ;   :options-id (keyword)}
  ; @param (*) option
  [options-id {:keys [get-label-f options-id] :as options-props} option]
  (let [option-label (get-label-f option)]
       [:button.x-select--option (engine/selectable-option-attributes options-id options-props option)
                                 [components/content option-label]]))

(defn- select-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) options-id
  ; @param (map) options-props
  ;  {:options (vector)}
  [options-id {:keys [options] :as options-props}]
  (letfn [(f [options option] (conj options [select-option options-id options-props option]))]
         (reduce f [:div.x-select--options] options)))

(defn- no-options-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) options-id
  ; @param (map) options-props
  ;  {:options (vector)}
  [_ {:keys [no-options-label]}]
  [:div.x-select--no-options-label [components/content no-options-label]])

(defn- select-options-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) options-id
  ; @param (map) options-props
  ;  {:options (vector)}
  [options-id {:keys [options] :as options-props}]
  (if (vector/nonempty? options)
      [select-options   options-id options-props]
      [no-options-label options-id options-props]))

(defn- select-options-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) options-id
  ; @param (map) options-props
  ;  {:options-id (keyword)}
  [options-id {:keys [options-id] :as options-props}]
  [engine/stated-element options-id
                         {:element-props options-props
                          :render-f      #'select-options-structure
                          :subscriber    [:elements/get-select-props options-id]}])



;; -- Select button components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [_ _]
  [:i.x-select--button-icon :unfold_more])

(defn- select-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [_ select-props]
  (let [button-label (select-props->select-button-label select-props)]
       [:div.x-select--button-label [components/content button-label]]))

(defn- select-button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  [:button.x-select--button-body (engine/clickable-body-attributes select-id select-props)
                                 [select-button-label              select-id select-props]
                                 [select-button-icon               select-id select-props]])

(defn- select-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  [:div.x-select--button [select-button-body    select-id select-props]
                         [engine/element-helper select-id select-props]])

(defn- select-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:label (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)}
  [_ {:keys [label required?]}]
  (if label [:div.x-select--label [components/content label]
                                  (if required? [:span.x-input--label-asterisk "*"])]))

(defn- select-layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  [:div.x-select (engine/element-attributes select-id select-props)
                 [select-label              select-id select-props]
                 [select-button             select-id select-props]])

(defn- select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:layout (keyword)}
  [select-id {:keys [layout] :as select-props}]
  (case layout :button      [button/element      select-id select-props]
               :icon-button [icon-button/element select-id select-props]
               :select      [select-layout       select-id select-props]))

(defn element
  ; A select elem gombja helyett lehetséges button elemet megjeleníteni az {:layout :button}
  ; vagy {:layout :icon-button} beállítás használatával.
  ;
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ;  {:autoclear? (boolean)(opt)
  ;    Default: false
  ;   :default-value (*)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :form-id (keyword)(opt)
  ;   :get-label-f (function)(constant)(opt)
  ;    Default: return
  ;   :get-value-f (function)(opt)
  ;    Default: return
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
  ;   :initial-options (vector)(constant)(opt)
  ;   :initial-value (*)(constant)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :button, :icon-button, :select
  ;    Default: :select
  ;   :no-options-label (metamorphic-content)(opt)
  ;     Default: :no-options
  ;   :on-popup-closed (metamorphic-event)(opt)
  ;   :on-select (metamorphic-event)(constant)(opt)
  ;   :options-label (metamorphic-content)(constant)(opt)
  ;   :options-path (vector)(constant)(opt)
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :user-cancel? (boolean)(constant)(opt)
  ;    Default: false
  ;    Only w/o {:options-label ...}
  ;   :value-path (vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/select {...}]
  ;
  ; @usage
  ;  [elements/select :my-select {...}]
  ;
  ; @usage
  ;  [elements/select {:icon         :sort
  ;                    :layout       :icon-button
  ;                    :options-path [:my-options]
  ;                    :value-path   [:my-selected-option]}]
  ([select-props]
   [element (a/id) select-props])

  ([select-id select-props]
   (let [select-props (select-props-prototype select-id select-props)]
        [engine/stated-element select-id
                               {:render-f      #'select
                                :element-props select-props
                                :initializer   [:elements/init-selectable! select-id]
                                :subscriber    [:elements/get-select-props select-id]}])))



; TODO
; A color selector elemhez hasonloan a kirenderelt popupnak nincs szüksége header elemre!
; A inicializálás ráér a popup kirenderelésekor



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/render-select-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; WARNING#4051
  ; Az [:elements/render-select-options!] esemény használatával nem lehetséges
  ; megfelően inicializálni a select elemet, mert a select-options elem nem használhatja
  ; az [:elements/init-selectable!] esemény initializer-ként, ugyanis az opciók minden
  ; kirenderelésekor újrainicializálná az elemet.
  ;
  ; WARNING#0134
  ;  A [select-options] elem az opció kiválasztása után lecsatolódik a React-fából,
  ;  ezért a tulajdonságai sem maradnak elérhetők a Re-Frame adatbázisban!
  ;  A lecsatolódás után az (r elements/get-input-value db :my-select) függvény
  ;  visszatérési értéke nil!
  ;
  ; @param (keyword)(opt) select-id
  ; @param (map) options-props
  [a/event-vector<-id]
  (fn [_ [_ select-id options-props]]
      (let [options-id    (engine/element-id->extended-id select-id :options)
            options-props (options-props-prototype        select-id options-props)]
           [:ui/add-popup! :elements/select-options
                           {:body   [select-options-body options-id options-props]
                            :header (if (options-props->render-popup-header? options-props)
                                        [select-options-header options-id options-props])
                            :initializer [:elements/reg-select-keypress-events!    options-id]
                            :desctructor [:elements/remove-select-keypress-events! options-id]
                            :min-width :xs}])))
