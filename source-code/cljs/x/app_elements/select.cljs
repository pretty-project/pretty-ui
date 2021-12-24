
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.19
; Description:
; Version: v0.9.2
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]
              [x.app-elements.button     :as button   :rename {element button}]
              [x.app-elements.label      :as label    :rename {element label}]
              [x.app-elements.polarity   :as polarity :rename {element polarity}]))



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
(def CLOSE-POPUP-DELAY 250)

; @constant (ms)
(def AUTOCLEAR-VALUE-DELAY 650)

; @constant (ms)
(def ON-POPUP-CLOSED-DELAY 600)

; @constant (metamorphic-content)
(def DEFAULT-SELECT-BUTTON-LABEL :select!)

; @constant (keyword)
(def SELECT-BUTTON-ICON :unfold_more)



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
  ;   :value-path (item-path vector)}
  ;
  ; @return (map)
  [select-id {:keys [autoclear? on-popup-closed on-select value-path]}]
  (let [popup-id (engine/element-id->extended-id select-id :popup)]
       {:dispatch-some  on-select
        :dispatch-later [{:ms CLOSE-POPUP-DELAY :dispatch [:ui/close-popup! popup-id]}
                         (when (boolean autoclear?) ; XXX#0134
                               {:ms AUTOCLEAR-VALUE-DELAY :dispatch [:db/remove-item! value-path]})
                         (when (some? on-popup-closed)
                               {:ms ON-POPUP-CLOSED-DELAY :dispatch on-popup-closed})]}))

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
          (or select-button-label DEFAULT-SELECT-BUTTON-LABEL)))



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
  ;  {:as-button? (boolean)(opt)
  ;   :value-path (item-path vector)}
  ;
  ; @return (map)
  ;  {:get-label-f (function)
  ;   :get-value-f (function)
  ;   :layout (keyword)
  ;   :value-path (item-path vector)}
  [select-id {:keys [as-button?] :as select-props}]
       ; BUG#1507
       ; Ha a select-button elem {:disabled? true} állapotban csatolódik a React-fába,
       ; akkor a {:disabled? true} tulajdonságát az options-props térképben továbbörökítené
       ; az {:on-click [:elements/render-select-options! ...]} konstans tulajdonságon keresztül
       ; select-options elemnek.
  (let [options-props (dissoc select-props :disabled?)]
       (merge {:get-label-f  return
               :get-value-f  return
               :options-path (engine/default-options-path select-id)
               :value-path   (engine/default-value-path   select-id)}
              ; A button elemnél is alkalmazott tulajdonságok csak akkor részei a select elem
              ; tulajdonságai prototípusának, ha a select elem nem button elemként jelenik meg.
              (if-not as-button? {:layout :row})
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
  ;   :on-select (metamorphic-event)
  ;   :options-id (keyword)
  ;   :options-path (item-path vector)
  ;   :user-cancel? (boolean)
  ;   :value-path (item-path vector)}
  [select-id {:keys [options-label] :as options-props}]
  (let [on-select (on-select-events select-id options-props)]
       (merge {:get-label-f  return
               :get-value-f  return
               :options-path (engine/default-options-path select-id)
               :value-path   (engine/default-value-path   select-id)}
              (if (nil? options-label) {:user-cancel? true})
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
  ; @param (keyword) popup-id
  ; @param (map) options-props
  ;
  ; @return (component)
  [popup-id _]
  [button {:preset   :close-icon-button
           :on-click [:ui/close-popup! popup-id]}])

(defn- select-options-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) options-props
  ;
  ; @return (component)
  [_ {:keys [options-label]}]
  [label {:content options-label}])

(defn- select-options-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) options-props
  ;
  ; @return (component)
  [popup-id {:keys [options-label user-cancel?] :as options-props}]
  (cond (some?   options-label) [polarity {:middle-content [select-options-label        popup-id options-props]}]
        (boolean user-cancel?)  [polarity {:end-content    [select-options-close-button popup-id options-props]}]))

(defn- select-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) select-props
  ;  {:get-label-f (function)
  ;   :options-id (keyword)}
  ; @param (*) option
  ;
  ; @return (hiccup)
  [popup-id {:keys [get-label-f options-id] :as select-props} option]
  (let [option-label (get-label-f option)]
       [:button.x-select--option (engine/selectable-option-attributes options-id select-props option)
                                 [components/content {:content option-label}]]))

(defn- select-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) select-props
  ;  {:options (maps in vector)}
  ;
  ; @return (hiccup)
  [popup-id {:keys [options] :as select-props} b]
  (vec (reduce #(conj %1 [select-option popup-id select-props %2])
                [:div.x-select--options]
                (param options))))

(defn- select-options-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) options-props
  ;  {:options-id (keyword)}
  ;
  ; @return (component)
  [popup-id {:keys [options-id] :as options-props}]
  [engine/stated-element options-id
                         {:component     #'select-options
                          :element-props options-props
                          :subscriber    [:elements/get-select-props options-id]}])



;; -- Select button components ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-button-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (hiccup)
  [_ _]
  [:i.x-select--button-icon SELECT-BUTTON-ICON])

(defn- select-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (hiccup)
  [_ select-props]
  (let [button-label (select-props->select-button-label select-props)]
       [:div.x-select--button-label [components/content {:content button-label}]]))

(defn- select-button-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (hiccup)
  [select-id select-props]
  [:button.x-select--button-body (engine/clickable-body-attributes select-id select-props)
                                 [select-button-label              select-id select-props]
                                 [select-button-icon               select-id select-props]])

(defn- select-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (hiccup)
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
  ;
  ; @return (hiccup)
  [_ {:keys [label required?]}]
  (if (some? label)
      [:div.x-select--label [components/content {:content label}]
                            (if required? [:span.x-input--label-asterisk "*"])]))

(defn- select-layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (hiccup)
  [select-id select-props]
  [:div.x-select (engine/element-attributes select-id select-props)
                 [select-label              select-id select-props]
                 [select-button             select-id select-props]])

(defn- select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:as-button? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [select-id {:keys [as-button?] :as select-props}]
  (if as-button? ; If {:as-button? true} ...
                 (let [button-props (as-> select-props % (engine/apply-preset button/BUTTON-PROPS-PRESETS %)
                                                         (button/button-props-prototype %))]
                      [button/button select-id button-props])
                 ; If {:as-button? false} ...
                 [select-layout select-id select-props]))

(defn element
  ; A select elem gombja helyett lehetséges button elemet megjeleníteni az {:as-button? true}
  ; tulajdonság használatával.
  ;
  ; @param (keyword)(opt) select-id
  ; @param (map) select-props
  ;  {:as-button? (boolean)(opt)
  ;    Default: false
  ;   :autoclear? (boolean)(opt)
  ;    Default: false
  ;   :default-value (*)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :form-id (keyword)(opt)
  ;   :get-label-f (function)(constant)(opt)
  ;    Default: return
  ;   :get-value-f (function)(opt)
  ;    Default: return
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :initial-options (vector)(constant)(opt)
  ;   :initial-value (*)(constant)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-popup-closed (metamorphic-event)(opt)
  ;   :on-select (metamorphic-event)(constant)(opt)
  ;   :options-label (metamorphic-content)(constant)(opt)
  ;   :options-path (item-path vector)(constant)(opt)
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :user-cancel? (boolean)(constant)(opt)
  ;    Default: true
  ;    Only w/o {:options-label ...}
  ;   :value-path (item-path vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/select {...}]
  ;
  ; @usage
  ;  [elements/select :my-select {...}]
  ;
  ; @usage
  ;  [elements/select {:as-button? true
  ;                    :icon       :sort
  ;                    :label      :order-by
  ;                    :layout     :icon-button
  ;                    :options-path [:my :options]
  ;                    :value-path   [:my :selected :option]}]
  ;
  ; @return (hiccup)
  ([select-props]
   [element (a/id) select-props])

  ([select-id select-props]
   (let [select-props (select-props-prototype select-id select-props)]
        [engine/stated-element select-id
                               {:component     #'select
                                :element-props select-props
                                :initializer   [:elements/init-selectable! select-id]
                                :subscriber    [:elements/get-select-props select-id]}])))



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
  (fn [_ event-vector]
      (let [select-id     (a/event-vector->second-id   event-vector)
            options-props (a/event-vector->first-props event-vector)
            options-id    (engine/element-id->extended-id select-id :popup)
            options-props (options-props-prototype        select-id options-props)]
           [:ui/add-popup! options-id
                           {:body   {:content #'select-options-body   :content-props options-props}
                            :header {:content #'select-options-header :content-props options-props}
                            :min-width :xs}])))
