
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns playground.view-selector.views
    (:require [app-fruits.dom        :as dom]
              [app-fruits.reagent    :as reagent :refer [ratom]]
              [mid-fruits.candy      :refer [param return]]
              [mid-fruits.form       :as form]
              [mid-fruits.gestures   :as gestures]
              [mid-fruits.keyword    :as keyword]
              [mid-fruits.loop       :as loop :refer [reduce-indexed]]
              [mid-fruits.map        :as map]
              [mid-fruits.math       :refer [calc]]
              [mid-fruits.mixed      :as mixed]
              [mid-fruits.pretty     :as pretty]
              [mid-fruits.random     :as random]
              [mid-fruits.string     :as string]
              [mid-fruits.time       :as time]
              [mid-fruits.vector     :as vector]
              [playground.engine     :as engine]
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
              [cljs-time.core :as cljs-time.core]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [view-id]
  [{:label "Anchors"    :on-click [:router/go-to! "/@app-home/playground/anchors"]    :active? (= view-id :anchors)}
   {:label "Buttons"    :on-click [:router/go-to! "/@app-home/playground/buttons"]    :active? (= view-id :buttons)}
   {:label "Chips"      :on-click [:router/go-to! "/@app-home/playground/chips"]      :active? (= view-id :chips)}
   {:label "Diagrams"   :on-click [:router/go-to! "/@app-home/playground/diagrams"]   :active? (= view-id :diagrams)}
   {:label "Expandable" :on-click [:router/go-to! "/@app-home/playground/expandable"] :active? (= view-id :expandable)}
   {:label "Fields"     :on-click [:router/go-to! "/@app-home/playground/fields"]     :active? (= view-id :fields)}
   {:label "Files"      :on-click [:router/go-to! "/@app-home/playground/files"]      :active? (= view-id :files)}
   {:label "Pickers"    :on-click [:router/go-to! "/@app-home/playground/pickers"]    :active? (= view-id :pickers)}
   {:label "Selectors"  :on-click [:router/go-to! "/@app-home/playground/selectors"]  :active? (= view-id :selectors)}
   {:label "Tables"     :on-click [:router/go-to! "/@app-home/playground/tables"]     :active? (= view-id :tables)}
   {:label "Text"       :on-click [:router/go-to! "/@app-home/playground/text"]       :active? (= view-id :text)}])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- section-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [label]}]
  [:<> [elements/label {:content label :font-weight :extra-bold :font-size :m :layout :fit}]
       [elements/horizontal-separator {:size :xxs}]
       [elements/horizontal-line      {:color :highlight :fade :out}]
       [elements/horizontal-separator {:size :m}]])

(defn- section-footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/horizontal-separator {:size :xxl}])

(defn- infinite-loader
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/text {:content "Infinite loader printed to console" :color :highlight :font-size :xs :layout :fit :selectable? false}]
       [tools/infinite-loader :playground {:on-viewport #(println "Playground infinite loader in viewport again!")}]
       [elements/button ::reload-infinite-loader-button
                        {:label "Reload infinite loader!" :on-click [:tools/reload-infinite-loader! :playground]
                         :variant :transparent :color :secondary :layout :fit}]])

(defn- anchors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/anchor {:content "Anchor link"   :href "/link"}]
       [elements/anchor {:content "Anchor button" :on-click [:router/go-to! "/link"]}]
       [elements/anchor {:content "Disabled anchor"  :on-click [] :disabled? true}]])

(defn- buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [section-footer]
       [section-header {:label "Icon-button"}]
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
       [section-footer]
       [section-header {:label "Filled button"}]
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
       [section-footer]
       [section-header {:label "Transparent button"}]
       [elements/button ::transparent-button-1
                        {:label "Transparent button #1"
                         :on-click [:developer/test!]
                         :variant :transparent}]
       [elements/button ::transparent-button-2
                        {:label "Transparent button #2"
                         :on-click [:developer/test!]
                         :variant :transparent
                         :icon :people}]
       [section-footer]
       [section-header {:label "Submit button"}]])

(defn- card-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [])
  ;[elements/card-group ::card-group
  ;                     {:cards [{:content "Card #1" :on-click [:developer/test!]}
  ;                              {:content "Card #2" :on-click [:developer/test!]}
  ;                              {:content "Card #3" :on-click [:developer/test!] :badge-color :secondary}])

(defn- chips
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/chip {:label "Chip" :icon :apps}]
       [elements/chip {:label "Deletable chip" :on-delete [:developer/test!]}]
       [elements/chip-group ::chip-group
                            {:label "Chip-group" :chips [{:label "Chip #1" :color :highlight}
                                                         {:label "Chip #2" :color :secondary}]
                             :on-delete [:developer/test!]}]])

(defn- diagrams
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [section-footer]
       [section-header {:label "Circle diagram"}]
       [elements/circle-diagram {:sections [{:color :primary   :value 50}
                                            {:color :secondary :value 25}
                                            {:color :highlight :value 25}]}]
       [section-footer]
       [section-header {:label "Line diagram"}]
       [elements/line-diagram {:sections [{:color :primary   :value 50}
                                          {:color :secondary :value 10}
                                          {:color :highlight :value 20}]}]
       [section-footer]
       [section-header {:label "Point diagram"}]
       [elements/point-diagram {:points [4000 4050 4150 4120 4150 4180]
                                :x-max 3800}]])

(defn- expandable
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/expandable {:content "Here comes the sun!" :label "Click to expand!" :icon :people}])

(defn- fields
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
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
                                  :get-label-f :x
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
                             :start-adornments [{:icon :sentiment_very_satisfied :on-click [:developer/test!] :tooltip "Hello"}]
                             :validator {:f #(= % "x")
                                         :invalid-message "Type \"x\""
                                         :pre-validate? true}}]
       [elements/password-field ::password-field-w-adornments
                                {:label "Password-field w/ adornments" :emptiable? true
                                 :placeholder "Placeholder"
                                 :start-adornments [{:icon :sentiment_very_satisfied :on-click [:developer/test!] :tooltip "Hello"}
                                                    {:label "Ft/m2"}
                                                    {:label "Ft/m2" :on-click []}]}]
       [elements/multiline-field ::multiline-field
                                 {:label "Multiline-field" :placeholder "Placeholder"}]
       [elements/digit-field {}]
       [elements/search-field ::search-field
                              {:label "Search-field" :placeholder "Placeholder"}]])

(defn- files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/file-drop-area {}]])

(defn- pickers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [section-footer]
       [section-header {:label "Color-picker"}]
       [elements/color-picker {:initial-options ["var( --background-color-primary )" "var( --background-color-secondary )" "var( --background-color-warning )" "var( --background-color-success )"]
                               :get-label-f return
                               :label "Color-picker"}]])

(defn- selectors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [section-footer]
       [section-header {:label "Select"}]
       [elements/select ::select
                        {:label "Select"
                         :on-select [:developer/test!]
                         :initial-options ["Option #1" "Option #2"]}]
       [section-footer]
       [section-header {:label "Switch"}]
       [elements/switch ::switch-1
                        {:label "Switch #1"
                         :info-tooltip "Lorem Ipsum is simply dummy text of the printing and typesetting industry."}]
       [elements/switch ::switch-2
                        {:label "Switch #2"
                         :info-tooltip "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                         :default-value true}]
       [section-footer]
       [section-header {:label "Checkbox"}]
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
       [section-footer]
       [section-header {:label "Checkbox-group"}]
;      [elements/checkbox-group ::checkbox-group
;                               {:label "Checkbox-group"
;                                :options-path (db/path ::stuff :stored-options)
;                                :get-label-f :x}]
       [section-footer]
       [section-header {:label "Radio-button"}]
       [elements/radio-button ::radio-button
                              {:label "Radio-button"
                               :unselectable? true
                               :get-label-f :label
                               :required? true
                               :initial-options [{:label "Option #1" :value "ot-1"}
                                                 {:label "Option #2" :value "ot-2"}]}]
       [section-footer]
       [section-header {:label "Counter"}]
       [elements/counter ::counter-1
                         {:label "Counter #1" :resetable? true :initial-value 420
                          :helper "Lorem Ipsum is simply dummy text of the printing and typesetting industry."}]
       [elements/counter ::counter-2
                         {:label "Counter #2" :resetable? true :initial-value 420}]])

(defn- tables
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [elements/data-table {:columns [{:label "Name"}
                                       {:label "Value #1"}
                                       {:label "Value #2"}]
                             :rows [["Data #1" 30 50]
                                    ["Data #2" 10 90]
                                    ["Data #3" 20 75]]}]])

(defn- text
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [section-footer]
       [section-header {:label "Label"}]
       [elements/label {:content "Follow the white rabbit!" :font-size :xxs}]
       [elements/label {:content "Follow the white rabbit!" :font-size :xs}]
       [elements/label {:content "Follow the white rabbit!" :font-size :s}]
       [elements/label {:content "Follow the white rabbit!" :font-size :m}]
       [elements/label {:content "Follow the white rabbit!" :font-size :l}]
       [elements/label {:content "Follow the white rabbit!" :font-size :xl}]
       [elements/label {:content "Follow the white rabbit!" :font-size :xxl}]
       [section-footer]
       [section-header {:label "Text"}]
       [elements/text {:content engine/LOREM-IPSUM :font-size :xxs :style {:max-width "720px"}}]
       [elements/horizontal-separator {:size :l}]
       [elements/text {:content engine/LOREM-IPSUM :font-size :xs  :style {:max-width "720px"}}]
       [elements/horizontal-separator {:size :l}]
       [elements/text {:content engine/LOREM-IPSUM :font-size :s   :style {:max-width "720px"}}]
       [elements/horizontal-separator {:size :l}]
       [elements/text {:content engine/LOREM-IPSUM :font-size :m   :style {:max-width "720px"}}]
       [elements/horizontal-separator {:size :l}]
       [elements/text {:content engine/LOREM-IPSUM :font-size :l   :style {:max-width "720px"}}]
       [elements/horizontal-separator {:size :l}]
       [elements/text {:content engine/LOREM-IPSUM :font-size :xl  :style {:max-width "720px"}}]
       [elements/horizontal-separator {:size :l}]
       [elements/text {:content engine/LOREM-IPSUM :font-size :xxl :style {:max-width "720px"}}]
       [section-footer]])

(defn debug-keypress-handler
  []
  (let [debug @(a/subscribe [:db/get-item [:environment/keypress-events]])]
       [:div {:style {:position :fixed :right 0 :bottom 0 :background :white :z-index 999 :font-size :10px}}
             [:pre (mid-fruits.pretty/mixed->string debug)]]))

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [view-id @(a/subscribe [:view-selector/get-selected-view-id :playground])]
       [elements/menu-bar {:menu-items (menu-items view-id)}]))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [view-id @(a/subscribe [:view-selector/get-selected-view-id :playground])]
       (case view-id :anchors    [anchors]
                     :buttons    [buttons]
                     :chips      [chips]
                     :diagrams   [diagrams]
                     :expandable [expandable]
                     :fields     [fields]
                     :files      [files]
                     :pickers    [pickers]
                     :selectors  [selectors]
                     :tables     [tables]
                     :text       [text])))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [:<> [layouts/layout-a {:body   #'body
                          :header #'header
                          :description "Follow the white rabbit!"
                          :horizontal-align :left}]
       [elements/horizontal-separator {:size :xxl}]
       [infinite-loader]
       [elements/horizontal-separator {:size :xxl}]
       [card-group]
       [elements/horizontal-separator {:size :xxl}]])

       ; DEBUG
       ;[debug-keypress-handler]])
