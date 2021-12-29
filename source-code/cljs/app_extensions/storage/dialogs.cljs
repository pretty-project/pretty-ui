
(ns app-extensions.storage.dialogs
    (:require [mid-fruits.io        :as io]
              [x.app-core.api       :as a :refer [r]]
              [x.app-dictionary.api :as dictionary]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/render-new-directory-name-dialog!
  (fn [{:keys [db]} _]
      [:value-editor/load-editor! :storage :new-directory-name
                                  {:label             :directory-name
                                   :save-button-label :create!
                                   ; Nem törli ki a value-editor az előzőnek bennt maradt értéket
                                   ;:initial-value (r dictionary/look-up db :new-directory)
                                   :on-save       [:storage/create-directory!]
                                   :validator {:f               io/directory-name-valid?
                                               :invalid-message :invalid-directory-name
                                               :pre-validate?   true}}]))
