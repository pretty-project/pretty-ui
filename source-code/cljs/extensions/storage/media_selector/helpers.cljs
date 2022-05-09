
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-selector.helpers
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-item->selection-icon
  [media-item]
  (if-let [file-selected? @(a/subscribe [:storage.media-selector/file-selected? media-item])]
          :check_circle_outline :radio_button_unchecked))
