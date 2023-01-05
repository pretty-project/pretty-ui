
(ns elements.text-field.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(defonce FIELD-CONTENTS (ratom {}))
