
(ns app-extensions.clients.dialogs
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-color-picker-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [popup-id]
  [elements/color-picker ::client-color-picker
                         {:initial-options ["var( --background-color-primary )"
                                            "var( --background-color-secondary )"
                                            "var( --background-color-success )"
                                            "var( --background-color-warning )"]
                          :on-select  [:ui/close-popup! popup-id]
                          :value-path [:clients :editor-data :client-color]}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :clients/render-client-color-picker!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/add-popup! ::client-color-picker
                  {:body {:content #'client-color-picker-body}
                   :min-width :none}])
