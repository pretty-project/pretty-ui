
(ns tools.file-saver.side-effects
    (:require [re-frame.api                  :as r]
              [tools.file-saver.helpers      :as helpers]
              [tools.file-saver.views        :as views]
              [tools.temporary-component.api :as temporary-component]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- save-accepted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  [saver-id saver-props]
  (temporary-component/append-component! [views/file-saver saver-id saver-props] helpers/save-file-f)
  (temporary-component/remove-component!))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :file-saver/save-accepted save-accepted)
