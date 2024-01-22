
(ns pretty-forms.views
    (:require [pretty-forms.env :as env]
              [metamorphic-content.api :as metamorphic-content]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn invalid-message
  ; @param (keyword) input-id
  ; @param (map) input-props
  [input-id _]
  (if-let [invalid-message (env/get-input-invalid-message input-id)]
          [:div {:class :pf-invalid-message :data-text-selectable false}
                [metamorphic-content/compose invalid-message]]))
