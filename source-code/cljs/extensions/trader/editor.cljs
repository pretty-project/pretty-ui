
(ns extensions.trader.editor
    (:require [dom.api                    :as dom]
              [extensions.trader.listener :as listener]
              [extensions.trader.styles   :as styles]
              [extensions.trader.sync     :as sync]
              [mid-fruits.candy           :refer [param return]]
              [mid-fruits.time            :as time]
              [x.app-components.api       :as components]
              [x.app-core.api             :as a :refer [r]]
              [x.app-elements.api         :as elements]
              [x.app-router.api           :as router]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def PLACEHOLDER "Wake up, Neo...\nThe Matrix has you\nFollow the white rabbit.\n\n\nKnock, knock, Neo.")



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-source-code
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:trader :editor :source-code]))

(defn- synchronized?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (some? (get-in db [:trader :editor])))

(defn- get-editor-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (merge (get-in db [:trader :editor])
         {:synchronized? (r synchronized? db)}))

(a/reg-sub :trader/get-editor-props get-editor-props)

(defn- get-editor-log-props
  [db _]
  (merge (r sync/get-response db :trader/download-log-data)
         (get-in db [:trader :listener])
         {:log-visible? (get-in db [:trader :editor :log-visible?] true)}))

(a/reg-sub :trader/get-editor-log-props get-editor-log-props)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-editor-log!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (if (some? (get-in db [:trader :editor :log-visible?]))
      (update-in db [:trader :editor :log-visible?] not)
      (assoc-in  db [:trader :editor :log-visible?] false)))

(a/reg-event-db :trader/toggle-editor-log! toggle-editor-log!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- test-source-code-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button {:layout :icon-button :icon :play_arrow :variant :transparent
                    :indent :right :color :invert :on-click [:trader/test-source-code!]
                    :tooltip "Test source code"}])

(defn- toggle-log-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [log-visible?]}]
  [elements/button {:layout :icon-button :icon :terminal :variant :transparent
                    :indent :right :color :invert :on-click [:trader/toggle-editor-log!]
                    :tooltip (if log-visible? "Hide log" "Show log")}])

(defn- editor-log-menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [:div {:style (styles/editor-log-menu-bar-style)}
        [test-source-code-button         module-id module-props]
        [toggle-log-button               module-id module-props]
        [listener/toggle-listener-button module-id module-props]])

(defn- editor-log-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ {:keys [log-entry timestamp] :as log-item}]
  [:div {:style {:display "flex"}}
        [:div {:style (styles/log-item-timestamp-style log-item)}
              (time/timestamp-string->time timestamp)]
        [:div {:style (styles/log-item-message-style log-item)}
              (str log-entry)]])

(defn- editor-log-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [log-items] :as module-props}]
  (reduce #(conj %1 [editor-log-item module-id module-props %2])
           [:div {:style (styles/editor-log-body-style)}]
           (param log-items)))

(defn- editor-log-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [:div {:style (styles/editor-log-style module-id module-props)}
        [editor-log-menu-bar module-id module-props]
        [editor-log-items    module-id module-props]])

(defn- editor-log
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [components/subscriber :trader/editor-log
                         {:render-f   #'editor-log-structure
                          :subscriber [:trader/get-editor-log-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- editor-textarea
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id {:keys [source-code synchronized?]}]
  [:textarea {:style (styles/editor-textarea-style)
              :value source-code
              :spellCheck "false" :placeholder (if synchronized? PLACEHOLDER)
              :on-change #(a/dispatch-sync [:db/set-item! [:trader :editor :source-code]
                                                          (dom/event->value %)])}])

(defn- upload-source-code-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [elements/button ::upload-source-code-button
                   {:layout :icon-button :icon :save :on-click [:trader/upload-source-code!]}])

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [elements/row {:content [:<> [upload-source-code-button module-id module-props]]
                 :horizontal-align :right :style {:padding-right "48px"}}])

(defn- editor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [:div {:style (styles/editor-style) :id "trader--editor"}
        [:style {:type "text/css"} (styles/editor-style-rules)]
        [:div {:style (styles/editor-structure-style)}
              [:div {:style (styles/editor-textarea-wrapper-style)}
                    [editor-textarea module-id module-props]]
              [editor-log module-id module-props]
              [:div {:style (styles/editor-menu-bar-style)}
                    [menu-bar module-id module-props]]]])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [view-id]
  [components/subscriber :trader/editor
                         {:render-f   #'editor
                          :subscriber [:trader/get-editor-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/edit-source-code!
  [:router/go-to! "/@app-home/trader/edit-source-code"])

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/load-editor!
  {:dispatch-n [[:db/remove-item! [:trader :editor]]
                [:ui/set-surface! ::view {:view {:content #'view}}]
                [:ui/set-title! "Source code editor"]]})

(a/reg-event-fx
  :trader/upload-source-code!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]}]
      [:sync/send-query! :trader/synchronize!
                         {:display-progress? true
                          :query [`(trader/upload-source-code! ~{:source-code (r get-source-code db)})]}]))

(a/reg-event-fx
  :trader/test-source-code!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]}]
      [:sync/send-query! :trader/synchronize!
                         {:display-progress? true
                          :query [`(trader/test-source-code! ~{:source-code (r get-source-code db)})]}]))
