
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-selector.helpers
    (:require [mid-fruits.candy :refer [return]]
              [x.app-core.api   :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-item->selection-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [media-item]
  (if-let [file-selected? @(a/subscribe [:storage.media-selector/file-selected? media-item])]
          (return :check_circle_outline)
          (return :radio_button_unchecked)))
