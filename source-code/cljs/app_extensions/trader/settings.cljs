
(ns app-extensions.trader.settings
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.map       :refer [dissoc-in]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.trader.engine :as engine]
              [app-extensions.trader.styles :as styles]
              [app-extensions.trader.sync   :as sync]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:trader :settings]))

(defn get-settings-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (merge (get-in db [:trader :settings])
         (r sync/get-response db :trader/get-settings-data)))

(a/reg-sub :trader/get-settings-props get-settings-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- symbol-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/select {:initial-options engine/SYMBOL-OPTIONS
                    :indent :both :get-label-f :label :min-width :s :label "Symbol"
                    :value-path [:trader :settings :symbol]}])

(defn sync-timeout-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/select ::sync-timeout-select
                   {:initial-options engine/SYNC-TIMEOUT-OPTIONS
                    :label "Sync timeout"
                    :get-label-f :label}])

(defn toggle-syncing-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [syncing?]}]
  [elements/button ::toggle-syncing-button
                   {:layout :icon-button
                    :color    (if syncing? :primary :muted)
                    :on-click [:trader/toggle-syncing!]
                    :icon :sync :indent :right :variant :transparent}])

(defn- save-settings-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _]
  [elements/button ::save-settings-button
                   {:label "Save"
                    :indent :right
                    :on-click [:trader/upload-settings!]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [module-id module-props]
  [:div {:style (styles/overlay-center-style)}
        [:div [symbol-select module-id module-props]
              [elements/horizontal-separator {:size :xxl}]
              [:div {:style (styles/row-style {:justify-content "flex-end"})}
                    [save-settings-button module-id module-props]]]
        [elements/horizontal-separator {:size :xxl}]])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id]
  [components/subscriber :trader/settings
                         {:render-f   #'settings
                          :subscriber [:trader/get-settings-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :trader/upload-settings!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      [:sync/send-query! :trader/synchronize!
                         {:display-progress? true
                          :query [:debug `(trader/upload-settings! ~(r get-settings db))]}]))
