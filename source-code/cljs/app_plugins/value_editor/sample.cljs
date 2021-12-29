
(ns app-plugins.value-editor.sample
    (:require [x.app-core.api :as a]
              [app-plugins.value-editor.api :as value-editor]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-my-props
  [db _]
  {:my-value (r value-editor/get-editor-value db :my-editor)})

(a/reg-sub :get-my-props get-my-props)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :edit-my-value!
  [:value-editor/load-editor! :my-extension :my-editor {:value-path [:my :item]}])

(a/reg-event-fx
  :edit-your-value!
  [:value-editor/load-editor! :my-extension :your-editor {:on-save [:->your-value-edited]}])



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :->your-value-edited
  (fn [_ [_ your-value]]
      [:do-something-with! your-value]))
