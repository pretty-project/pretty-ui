
(ns components.side-menu-button.presets)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BUTTON-PROPS-PRESETS
     {:add       {:icon        :add
                  :label       :add!}
      :delete    {:color       :warning
                  :icon        :delete_outline
                  :label       :delete!}
      :duplicate {:icon        :file_copy
                  :label       :duplicate!}
      :edit      {:icon        :edit
                  :label       :edit!}
      :revert    {:icon        :settings_backup_restore
                  :label       :revert!}
      :save      {:icon        :save
                  :label       :save!}})
