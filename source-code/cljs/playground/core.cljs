
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
              [app-extensions.clients.api]
              [app-extensions.home.api]
              [app-extensions.media.api]
              [app-extensions.products.api]
              [app-extensions.settings.api]
              [app-extensions.trader.api]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def LOREM-IPSUM "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [view-id]}]
  [{:label "Anchors"    :on-click [:router/go-to! "/:app-home/playground/anchors"]    :active? (= view-id :anchors)}
   {:label "Buttons"    :on-click [:router/go-to! "/:app-home/playground/buttons"]    :active? (= view-id :buttons)}
   {:label "Chips"      :on-click [:router/go-to! "/:app-home/playground/chips"]      :active? (= view-id :chips)}
   {:label "Diagrams"   :on-click [:router/go-to! "/:app-home/playground/diagrams"]   :active? (= view-id :diagrams)}
   {:label "Expandable" :on-click [:router/go-to! "/:app-home/playground/expandable"] :active? (= view-id :expandable)}
   {:label "Fields"     :on-click [:router/go-to! "/:app-home/playground/fields"]     :active? (= view-id :fields)}
   {:label "Files"      :on-click [:router/go-to! "/:app-home/playground/files"]      :active? (= view-id :files)}
   {:label "Pickers"    :on-click [:router/go-to! "/:app-home/playground/pickers"]    :active? (= view-id :pickers)}
   {:label "Selectors"  :on-click [:router/go-to! "/:app-home/playground/selectors"]  :active? (= view-id :selectors)}
   {:label "Tables"     :on-click [:router/go-to! "/:app-home/playground/tables"]     :active? (= view-id :tables)}
   {:label "Text"       :on-click [:router/go-to! "/:app-home/playground/text"]       :active? (= view-id :text)}])



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
       [elements/horizontal-separator {:size :xxs}]
       [elements/horizontal-line      {:color :highlight :fade :out}]
       [elements/horizontal-separator {:size :m}]])

(defn- section-footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/horizontal-separator {:size :xxl}])

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
       [elements/button ::primary-filled-button
                        {:label "Filled button" :variant :filled :background-color :primary :icon :people
                         :on-click [:developer/test!]}]
       [elements/button ::secondary-filled-button
                        {:label "Filled button" :variant :filled :background-color :secondary :icon :dashboard
                         :on-click [:developer/test!]}]
       [elements/button ::success-filled-button
                        {:label "Filled button" :variant :filled :background-color :success :icon :people
                         :on-click [:developer/test!]}]
       [elements/button ::warning-filled-button
                        {:label "Filled button" :variant :filled :background-color :warning :icon :people
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
                         :icon :people}]
       [section-footer surface-id {}]
       [section-header surface-id {:label "Submit button"}]])

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
  [surface-id _]
  [:<> [section-footer surface-id {}]
       [section-header surface-id {:label "Circle diagram"}]
       [elements/circle-diagram {:sections [{:color :primary   :value 50}
                                            {:color :secondary :value 10}
                                            {:color :highlight :value 20}]}]
       [section-footer surface-id {}]
       [section-header surface-id {:label "Line diagram"}]
       [elements/line-diagram {:sections [{:color :primary   :value 50}
                                          {:color :secondary :value 10}
                                          {:color :highlight :value 20}]}]
       [section-footer surface-id {}]
       [section-header surface-id {:label "Point diagram"}]
       [elements/point-diagram {:points [4000 4050 4150 4120 4150 4180]
                                :x-max 3800}]])

(defn- expandable
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/expandable {:content "Here comes the sun!" :label "Click to expand!" :icon :people}])

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

(defn- files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [:<> [elements/file-drop-area {}]])

(defn- pickers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id _]
  [:<> [section-footer surface-id {}]
       [section-header surface-id {:label "Color-picker"}]
       [elements/color-picker {:initial-options ["var( --background-color-primary )" "var( --background-color-secondary )" "var( --background-color-warning )" "var( --background-color-success )"]
                               :get-label-f return
                               :label "Color-picker"}]])

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
                               :required? true
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
  [surface-id _]
  [:<> [section-footer surface-id {}]
       [section-header surface-id {:label "Label"}]
       [elements/label {:content "Follow the white rabbit!" :font-size :xxs}]
       [elements/label {:content "Follow the white rabbit!" :font-size :xs}]
       [elements/label {:content "Follow the white rabbit!" :font-size :s}]
       [elements/label {:content "Follow the white rabbit!" :font-size :m}]
       [elements/label {:content "Follow the white rabbit!" :font-size :l}]
       [elements/label {:content "Follow the white rabbit!" :font-size :xl}]
       [elements/label {:content "Follow the white rabbit!" :font-size :xxl}]
       [section-footer surface-id {}]
       [section-header surface-id {:label "Text"}]
       [elements/text {:content LOREM-IPSUM :font-size :xxs :style {:max-width "720px"}}]
       [elements/horizontal-separator {:size :l}]
       [elements/text {:content LOREM-IPSUM :font-size :xs  :style {:max-width "720px"}}]
       [elements/horizontal-separator {:size :l}]
       [elements/text {:content LOREM-IPSUM :font-size :s   :style {:max-width "720px"}}]
       [elements/horizontal-separator {:size :l}]
       [elements/text {:content LOREM-IPSUM :font-size :m   :style {:max-width "720px"}}]
       [elements/horizontal-separator {:size :l}]
       [elements/text {:content LOREM-IPSUM :font-size :l   :style {:max-width "720px"}}]
       [elements/horizontal-separator {:size :l}]
       [elements/text {:content LOREM-IPSUM :font-size :xl  :style {:max-width "720px"}}]
       [elements/horizontal-separator {:size :l}]
       [elements/text {:content LOREM-IPSUM :font-size :xxl :style {:max-width "720px"}}]
       [section-footer surface-id {}]])

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
                :pickers    [pickers    surface-id view-props]
                :selectors  [selectors  surface-id view-props]
                :tables     [tables     surface-id view-props]
                :text       [text       surface-id view-props]))

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props]
  [:<> [layouts/layout-a surface-id
                         {:description "Follow the white rabbit!"
                          :body   {:content #'body   :subscriber [:view-selector/get-view-props :playground]}
                          :header {:content #'header :subscriber [:view-selector/get-view-props :playground]}
                          :horizontal-align :left}]
       [elements/horizontal-separator {:size :xxl}]
       [infinite-loader surface-id view-props]
       [elements/horizontal-separator {:size :xxl}]
       [card-group      surface-id view-props]
       [elements/horizontal-separator {:size :xxl}]])



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

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:environment/add-external-css! "/css/playground.css"]})
