
(ns playground.core
    (:require [app-fruits.dom        :as dom]
              [app-fruits.reagent    :as reagent :refer [ratom]]
              [mid-fruits.candy      :refer [param return]]
              [mid-fruits.form       :as form]
              [mid-fruits.gestures   :as gestures]
              [mid-fruits.keyword    :as keyword]
              [mid-fruits.loop       :as loop :refer [reduce-indexed reduce-while reduce-while-indexed]]
              [mid-fruits.map        :as map]
              [mid-fruits.math       :refer [calc]]
              [mid-fruits.mixed      :as mixed]
              [mid-fruits.pretty     :as pretty]
              [mid-fruits.random     :as random]
              [mid-fruits.string     :as string]
              [mid-fruits.time       :as time]
              [mid-fruits.vector     :as vector]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-developer.api   :as developer]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-layouts.api     :as layouts]
              [x.app-locales.api     :as locales]
              [x.app-sync.api        :as sync]
              [x.app-tools.api       :as tools]
              [x.app-ui.api          :as ui]

              ; TEMP
              [cljs-time.core :as cljs-time.core]

              ; A project-emulator.core fájl közös használatának mellőzése miatt a fejlesztés alatt
              ; lévő modulok behívása a playground.core névtérben történik.
              [extensions.clients.api]
              [extensions.home.api]
              [extensions.media.api]
              [extensions.products.api]
              [extensions.settings.api]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [view-id]}]
  [{:label "Anchors"    :on-click [:router/go-to! "/playground/anchors"]    :active? (= view-id :anchors)}
   {:label "Buttons"    :on-click [:router/go-to! "/playground/buttons"]    :active? (= view-id :buttons)}
   {:label "Chips"      :on-click [:router/go-to! "/playground/chips"]      :active? (= view-id :chips)}
   {:label "Diagrams"   :on-click [:router/go-to! "/playground/diagrams"]   :active? (= view-id :diagrams)}
   {:label "Expandable" :on-click [:router/go-to! "/playground/expandable"] :active? (= view-id :expandable)}
   {:label "Fields"     :on-click [:router/go-to! "/playground/fields"]     :active? (= view-id :fields)}
   {:label "Files"      :on-click [:router/go-to! "/playground/files"]      :active? (= view-id :files)}
   {:label "Selectors"  :on-click [:router/go-to! "/playground/selectors"]  :active? (= view-id :selectors)}
   {:label "Tables"     :on-click [:router/go-to! "/playground/tables"]     :active? (= view-id :tables)}
   {:label "Text"       :on-click [:router/go-to! "/playground/text"]       :active? (= view-id :text)}])



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:debug (get-in db [:playground :debug])})

(a/reg-sub ::get-view-props get-view-props)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :playground/test!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:dispatch [:developer/test!]}))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- section-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [label]}]
  [:<> [elements/label {:content label :font-weight :extra-bold :font-size :m :layout :fit}]
       [elements/separator {:orientation :horizontal :size :xxs}]
       [elements/horizontal-line {:color :highlight :fade :out}]
       [elements/separator {:orientation :horizontal :size :m}]])

(defn- section-footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/separator {:orientation :horizontal :size :xxl}])

(defn- infinite-loader
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [elements/text {:content "Infinite loader printed to console" :color :highlight :font-size :xs :layout :fit :selectable? false}]
       [tools/infinite-loader :playground-loader {:on-viewport #(println "Playground infinite loader in viewport again!")}]
       [elements/button ::reload-infinite-loader-button
                        {:label "Reload infinite loader!" :on-click [:tools/reload-infinite-loader! :playground-loader]
                         :variant :transparent :color :secondary :layout :fit}]])

(defn- anchors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [elements/anchor {:content "Anchor link"   :href "/link"}]
       [elements/anchor {:content "Anchor button" :on-click [:router/go-to! "/link"]}]
       [elements/anchor {:content "Disabled anchor"  :on-click [] :disabled? true}]])

(defn- buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id _]
  [:<> [section-footer surface-id {}]
       [section-header surface-id {:label "Icon-button"}]
       [elements/button ::pres-esc-button
                        {:label "Press ESC" :keypress {:key-code 27} :layout :icon-button
                         :variant :transparent :color :none :icon :people :on-click [:developer/test!]}]
       [:div {:style {:display :flex}}
             [elements/button ::add-icon-button
                              {:tooltip :add! :preset :add-icon-button :on-click [:developer/test!]}]
             [elements/button ::save-icon-button
                              {:tooltip :save! :preset :save-icon-button :on-click [:developer/test!]}]
             [elements/button ::delete-icon-button
                              {:tooltip :delete! :preset :delete-icon-button :on-click [:developer/test!]}]]
       [section-footer surface-id {}]
       [section-header surface-id {:label "Filled button"}]
       [elements/button ::filled-button
                        {:label "Filled button" :variant :filled :color :primary :icon :people
                         :on-click [:developer/test!]}]
       [section-footer surface-id {}]
       [section-header surface-id {:label "Outlined button"}]
       [elements/button ::outlined-button
                        {:label "Outlined button"
                         :on-click [:developer/test!]}]
       [section-footer surface-id {}]
       [section-header surface-id {:label "Transparent button"}]
       [elements/button ::transparent-button-1
                        {:label "Transparent button #1"
                         :on-click [:developer/test!]
                         :variant :transparent}]
       [elements/button ::transparent-button-2
                        {:label "Transparent button #2"
                         :on-click [:developer/test!]
                         :variant :transparent
                         :icon :people}]])

(defn- card-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/card-group ::card-group
                       {:cards [{:content "Card #1" :on-click [:developer/test!]}
                                {:content "Card #2" :on-click [:developer/test!]}
                                {:content "Card #3" :on-click [:developer/test!] :badge-color :secondary}]}])

(defn- chips
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [elements/chip {:label "Chip" :icon :apps}]
       [elements/chip {:label "Deletable chip" :on-delete [:developer/test!]}]
       [elements/chip-group ::chip-group
                            {:label "Chip-group" :chips [{:label "Chip #1" :color :highlight}
                                                         {:label "Chip #2" :color :secondary}]
                             :on-delete [:developer/test!]}]])

(defn- diagrams
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [elements/circle-diagram {:sections [{:color :primary :value 50}
                                            {:color :highlight :value 20}]}]
       [elements/line-diagram {:sections [{:color :primary :value 50}
                                          {:color :highlight :value 20}]}]])

(defn- expandable
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/expandable {:content "Here comes the sun!" :label "Click to expand!"}])

(defn- files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [elements/file-drop-area {}]])

(defn- selectors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id _]
  [:<> [section-footer surface-id {}]
       [section-header surface-id {:label "Select"}]
       [elements/select ::select
                        {:label "Select"
                         :on-select [:developer/test!]
                         :initial-options ["Option #1" "Option #2"]}]
       [section-footer surface-id {}]
       [section-header surface-id {:label "Switch"}]
       [elements/switch ::switch-1
                        {:label "Switch #1"
                         :info-tooltip "Lorem Ipsum is simply dummy text of the printing and typesetting industry."}]
       [elements/switch ::switch-2
                        {:label "Switch #2"
                         :info-tooltip "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                         :default-value true}]
       [section-footer surface-id {}]
       [section-header surface-id {:label "Checkbox"}]
       [elements/checkbox ::checkbox-1
                          {:label "Checkbox #1"
                           :helper "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                           :required? true}]
       [elements/checkbox ::checkbox-2
                          {:label "Checkbox #2"
                           :helper "Check it to check out!"}]
       [elements/checkbox ::checkbox-3
                          {:label "Checkbox #3"
                           :helper "Check it to check out!"}]
       [section-footer surface-id {}]
       [section-header surface-id {:label "Checkbox-group"}]
;      [elements/checkbox-group ::checkbox-group
;                               {:label "Checkbox-group"
;                                :options-path (db/path ::stuff :stored-options)
;                                :get-label-f :x}]
       [section-footer surface-id {}]
       [section-header surface-id {:label "Radio-button"}]
       [elements/radio-button ::radio-button
                              {:label "Radio-button"
                               :unselectable? true
                               :get-label-f :label
                               :initial-options [{:label "Option #1" :value "ot-1"}
                                                 {:label "Option #2" :value "ot-2"}]}]
       [section-footer surface-id {}]
       [section-header surface-id {:label "Counter"}]
       [elements/counter ::counter-1
                         {:label "Counter #1" :resetable? true :initial-value 420
                          :helper "Lorem Ipsum is simply dummy text of the printing and typesetting industry."}]
       [elements/counter ::counter-2
                         {:label "Counter #2" :resetable? true :initial-value 420}]])

(defn- tables
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [elements/data-table {:columns [{:label "Name"}
                                       {:label "Value #1"}
                                       {:label "Value #2"}]
                             :rows [["Data #1" 30 50]
                                    ["Data #2" 10 90]
                                    ["Data #3" 20 75]]}]])

(defn- text
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [elements/label {:content "Label"}]])

(defn- fields
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [elements/multi-field ::multi-field
                             {:label "Multi-field"
                              :value-path [:x :y]}]
       [elements/combo-box ::combo-box
                           {:label "Combo-box"
                            :get-label-f  #(do (get % :x))
                            :options-path (db/path ::stuff :initial-options)
                            :initial-options [{:x "A"} {:x "B"}]
                            :initial-value {:x "B"}}]
       [elements/multi-combo-box ::multi-combo-box
                                 {:label "Multi-combo-box"
                                  :get-label-f #(get % :x)
                                  :options-path (db/path ::stuff :initial-options)}]
       [elements/date-field ::date-field
                            {:label "Date field" :value-path (db/path ::stuff :date)}]
       [elements/text-field ::text-field-w-surface
                            {:label "Text-field w/ surface" :emptiable? true
                             :placeholder "Placeholder"
                             :surface {:content [:<> [:div {:style {:padding "24px 12px"}} "Text field surface"]]}
                             :helper "My helper"}]
       [elements/text-field ::text-field-w-modifier
                            {:label "Text-field w/ modifier" :emptiable? true
                             :placeholder "Placeholder"
                             :modifier #(string/starts-with! % "/")}]
       [elements/text-field ::text-field-w-validator
                            {:label "Text-field w/ validator" :emptiable? true
                             :placeholder "Placeholder"
                             :validator {:f #(= % "x")
                                         :invalid-message "Type \"x\""}}]
       [elements/text-field ::text-field-w-prevalidator
                            {:label "Text-field w/ prevalidator" :emptiable? true
                             :placeholder "Placeholder"
                             :initial-value "x"
                             :validator {:f #(= % "x")
                                         :invalid-message "Type \"x\""
                                         :pre-validate? true}}]
       [elements/password-field ::password-field-w-adornments
                                {:label "Password-field w/ adornments" :emptiable? true
                                 :placeholder "Placeholder"
                                 :start-adornments [{:icon :sentiment_very_satisfied :on-click [:developer/test!] :tooltip "Hello"}]}]
       [elements/multiline-field ::multiline-field
                                 {:label "Multiline-field" :placeholder "Placeholder"}]
       [elements/digit-field {}]
       [elements/search-field ::search-field
                              {:label "Search-field" :placeholder "Placeholder"}]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [elements/menu-bar {:menu-items (menu-items surface-id view-props)}])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [view-id] :as view-props}]
  (case view-id :anchors    [anchors    surface-id view-props]
                :buttons    [buttons    surface-id view-props]
                :chips      [chips      surface-id view-props]
                :diagrams   [diagrams   surface-id view-props]
                :expandable [expandable surface-id view-props]
                :fields     [fields     surface-id view-props]
                :files      [files      surface-id view-props]
                :selectors  [selectors  surface-id view-props]
                :tables     [tables     surface-id view-props]
                :text       [text       surface-id view-props]))

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [:<> [layouts/layout-a surface-id
                         {:description "Follow the white rabbit!"
                          :body   {:content #'body   :subscriber [:view-selector/get-view-props :playground]}
                          :header {:content #'header :subscriber [:view-selector/get-view-props :playground]}}]
       [elements/separator {:orientation :horizontal :size :xxl}]
       [infinite-loader surface-id view-props]
       [elements/separator {:orientation :horizontal :size :xxl}]
       [card-group      surface-id view-props]
       [elements/separator {:orientation :horizontal :size :xxl}]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (assoc-in db (db/path ::stuff :stored-options)
               [{:x "Apple"}
                {:x "Apple juice"}
                {:x "Pineapple"}
                {:x "Banana"}
                {:x "Brown"}
                {:x "Apocalypse"}]))

(a/reg-event-db :playground/initialize! initialize!)

(a/reg-event-fx
  :playground/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! ::view {:view        {:content #'view :subscriber [::get-view-props]}
                            :initializer [:playground/initialize!]}])

(a/reg-event-fx
  :playground/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:ui/set-header-title! :playground]
                [:ui/set-window-title! :playground]
                [:playground/render!]]})
