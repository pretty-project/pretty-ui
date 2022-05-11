
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.alias-editor.effects
    (:require [extensions.storage.alias-editor.views :as alias-editor.views]
              [x.app-core.api                        :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.alias-editor/load-editor!
  (fn [{:keys [db]} [_ media-item]]
      [:ui/render-popup! :storage.alias-editor/view
                         {:content [alias-editor.views/view media-item]}]))

(a/reg-event-fx
  :storage.alias-editor/update-item-alias!
  (fn [{:keys [db]} [_ {:keys [alias id] :as media-item}]]
      (let [updated-alias (get-in db [:storage :alias-editor/item-alias])]
           {:dispatch [:ui/close-popup! :storage.alias-editor/view]
            :dispatch-if [(not= alias updated-alias)
                          [:item-browser/update-item! :storage.media-browser id {:alias updated-alias}]]})))
