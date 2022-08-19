
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.file-saver.side-effects
    (:require [x.app-core.api                               :as a]
              [x.app-tools.file-saver.helpers               :as file-saver.helpers]
              [x.app-tools.file-saver.views                 :as file-saver.views]
              [x.app-tools.temporary-component.side-effects :refer [append-temporary-component! remove-temporary-component!]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- save-file-accepted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) saver-id
  ; @param (map) saver-props
  [saver-id saver-props]
  (append-temporary-component! [file-saver.views/file-saver saver-id saver-props] file-saver.helpers/save-file-f)
  (remove-temporary-component!))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :tools/save-file-accepted save-file-accepted)
