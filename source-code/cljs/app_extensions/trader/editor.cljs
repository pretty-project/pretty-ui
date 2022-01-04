
(ns app-extensions.trader.editor
    (:require [app-fruits.dom       :as dom]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-router.api     :as router]
              [app-extensions.trader.styles :as styles]
              [app-extensions.trader.sync   :as sync]))



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
  [elements/row {:content [upload-source-code-button module-id module-props]
                 :horizontal-align :right :style {:padding-right "96px"}}])

(defn- editor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [:div {:style (styles/editor-style)}
        [editor-textarea module-id module-props]
        [:div {:style (styles/editor-menu-bar-style)}
              [menu-bar module-id module-props]]])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [view-id]
  [components/subscriber :trader/editor
                         {:component #'editor
                          :subscriber [:trader/get-editor-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/edit-source-code!
  [:router/go-to! "/:app-home/trader/edit-source-code"])

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/receive-editor-data!
  (fn [{:keys [db]} [_ response]]
      (let [editor-data (get response :trader/get-editor-data)]
           {:db (assoc-in db [:trader :editor] editor-data)})))

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/request-editor-data!
  [:sync/send-query! :trader/synchronize!
                     {:query      [:debug `(:trader/get-editor-data ~{})]
                      :on-success [:trader/receive-editor-data!]}])

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :trader/load-editor!
  {:dispatch-n [[:db/remove-item! [:trader :editor]]
                [:ui/set-surface! ::view {:view {:content #'view}}]
                [:trader/request-editor-data!]]})

(a/reg-event-fx
  :trader/upload-source-code!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]}]
      [:sync/send-query! :trader/synchronize!
                         {:query [:debug `(:trader/upload-source-code! ~{:source-code (r get-source-code db)})]}]))
