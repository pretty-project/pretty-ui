
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.directory-creator.dialogs
    (:require [app-plugins.value-editor.api]
              [mid-fruits.io        :as io]
              [x.app-core.api       :as a :refer [r]]
              [x.app-dictionary.api :as dictionary]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.directory-creator/render-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ creator-id]]
      [:value-editor/load-editor! :storage :directory-name
                                  {:label :directory-name :save-button-label :create!
                                   :initial-value (r dictionary/look-up db :new-directory)
                                   :on-save       [:storage.directory-creator/create-directory! creator-id]
                                   :validator {:f io/directory-name-valid?
                                               :invalid-message :invalid-directory-name
                                               :pre-validate?   true}}]))
