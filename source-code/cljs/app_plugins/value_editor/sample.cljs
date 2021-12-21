
(ns app-plugins.value-editor.sample
    (:require [x.app-core.api :as a]
              [app-plugins.value-editor.api :as value-editor]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/dispatch [:value-editor/load! {}])



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-my-props
  [db _]
  {:my-value (r value-editor/get-editor-value db :my-editor)})

(a/reg-sub :get-my-props get-my-props)
